package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.engine.path.Path

trait Import[DeviceId, Token]:
  def sender: DeviceId
  def messages: Map[Path[Token], Any]
