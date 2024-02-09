package it.unibo.scafi.xc.engine

import it.unibo.scafi.xc.engine.network.Export

trait Context[DeviceId, Token, Value]:
  def messages: Export[DeviceId, Token, Value]
