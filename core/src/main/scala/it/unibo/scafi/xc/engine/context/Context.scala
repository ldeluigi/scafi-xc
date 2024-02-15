package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.engine.network.Export

trait Context[DeviceId, Token, Value]:
  def inboundMessages: Export[DeviceId, Token, Value]
  def outboundMessages: Export[DeviceId, Token, Value]
