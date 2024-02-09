package it.unibo.scafi.xc.engine.network

trait Network[DeviceId, Token, Value]:
  type OutboundMessage = Export[DeviceId, Token, Value]
  type InboundMessage = Import[DeviceId, Token, Value]

  def localId: DeviceId

  def send(e: OutboundMessage): Unit

  def receive(): Iterable[InboundMessage]
