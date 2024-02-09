package it.unibo.scafi.xc.engine

import it.unibo.scafi.xc.engine.network.{ Export, Import, Network }

class BaseEngine[DeviceId, Result, Token, Value, C <: Context[DeviceId, Token, Value]](
    private val net: Network[DeviceId, Token, Value],
    private val factory: (DeviceId, Import[DeviceId, Token, Value]) => C,
    private val program: C ?=> Result,
):

  private def round(): AggregateResult =
    val inMessages = net.receive()
    given c: C = factory(net.localId, inMessages)
    val result = program
    val outMessages = c.messages
    net.send(outMessages)
    AggregateResult(result, inMessages, outMessages)

  def cycle(): Result = round().result

  def cycleWhile(p: AggregateResult => Boolean): Result =
    var ar = round()
    while p(ar) do ar = round()
    ar.result

  case class AggregateResult(
      result: Result,
      incomingMessages: Import[DeviceId, Token, Value],
      outgoingMessages: Export[DeviceId, Token, Value],
  )
end BaseEngine
