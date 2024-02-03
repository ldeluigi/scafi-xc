package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.engine.path.{ MessagesToNeighbors, ValueTree }

trait Export[DeviceId, Token]:
  def sender: DeviceId
  def messages: ValueTree[Token, MessagesToNeighbors[DeviceId]]
