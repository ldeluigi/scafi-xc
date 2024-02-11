package it.unibo.scafi.xc.engine

import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.network.{ Export, Import, Network }

class Engine[
    DeviceId,
    Result,
    Token,
    Value,
    N <: Network[DeviceId, Token, Value],
    C <: Context[DeviceId, Token, Value],
](
    private val net: N,
    private val factory: ContextFactory[N, C],
    private val program: C ?=> Result,
):

  private def round(): AggregateResult =
    given c: C = factory.create(net)
    val result: Result = program
    val outMessages = c.outboundMessages
    net.send(outMessages)
    AggregateResult(result, c.inboundMessages, outMessages)

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
end Engine
