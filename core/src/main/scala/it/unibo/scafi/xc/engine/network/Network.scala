package it.unibo.scafi.xc.engine.network

trait Network[DeviceId]:
  type OutboundMessage = Export[DeviceId, Any, Any]
  type InboundMessage = Import[DeviceId, Any, Any]

  def send(e: OutboundMessage): Unit

  def receive(): Iterable[InboundMessage]
