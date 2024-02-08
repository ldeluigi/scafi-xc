package it.unibo.scafi.xc.engine

import it.unibo.scafi.xc.engine.network.Network

trait BaseEngine[DeviceId, Result, C <: Context[DeviceId]](
    val net: Network[DeviceId],
    val deviceId: DeviceId,
    val factory: Iterable[net.InboundMessage] => C,
):
  def program: C ?=> Result

  def cycle(): Result =
    val messages = net.receive()
    given c: C = factory(messages)
    val result = program
    net.send(c.messages)
    result
