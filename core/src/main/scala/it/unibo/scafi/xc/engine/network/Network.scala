package it.unibo.scafi.xc.engine.network

trait Network:
  type DeviceId
  type Token
  def send(e: Export[DeviceId, Token]): Unit

  def receive(): Iterable[Import[DeviceId, Token]]
