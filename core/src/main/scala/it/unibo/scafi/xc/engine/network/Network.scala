package it.unibo.scafi.xc.engine.network

trait Network:
  type DeviceId
  type Token
  type Value
  def send(e: Export[DeviceId, Token, Value]): Unit

  def receive(): Iterable[Import[DeviceId, Token, Value]]
