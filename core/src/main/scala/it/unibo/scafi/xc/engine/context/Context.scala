package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.engine.network.{ Export, Import }

trait Context[DeviceId, Token, Value]:
  def inboundMessages: Import[DeviceId, Token, Value]
  def outboundMessages: Export[DeviceId, Token, Value]
