package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.engine.path.ValueTree

trait Import[DeviceId, Token]:
  def sender: DeviceId
  def messages: ValueTree[Token, Any]
