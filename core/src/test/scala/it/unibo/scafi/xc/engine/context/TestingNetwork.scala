package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.network.{ Export, Network }

class TestingNetwork[Id, Tokens, Values](
    var localId: Id,
    var received: Export[Id, Tokens, Values] = Map.empty,
) extends Network[Id, Tokens, Values]:
  var sent: Export[Id, Tokens, Values] = Map.empty

  override def send(e: Export[Id, Tokens, Values]): Unit = sent = e

  override def receive(): Export[Id, Tokens, Values] = received
