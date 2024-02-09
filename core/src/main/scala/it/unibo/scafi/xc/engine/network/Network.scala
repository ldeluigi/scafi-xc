package it.unibo.scafi.xc.engine.network

trait Network[DeviceId, Token, Value]:
  def localId: DeviceId

  def send(e: Export[DeviceId, Token, Value]): Unit

  def receive(): Import[DeviceId, Token, Value]
