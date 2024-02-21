package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.network.{ Export, Import, Network }

class TestingNetwork[Id, Tokens, Values](
    var localId: Id,
    var received: Import[Id, Tokens, Values] = Map.empty,
) extends Network[Id, Tokens, Values]:
  var sent: Export[Id, Tokens, Values] = MapWithDefault.empty(ValueTree.empty)

  override def send(e: Export[Id, Tokens, Values]): Unit = sent = e

  override def receive(): Import[Id, Tokens, Values] = received
