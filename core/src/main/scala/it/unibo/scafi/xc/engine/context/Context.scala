package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.engine.network.Import

trait Context[DeviceId, Token, Value]:
  def inboundMessages: Import[DeviceId, Token, Value]
  def outboundMessages: Import[DeviceId, Token, Value]
