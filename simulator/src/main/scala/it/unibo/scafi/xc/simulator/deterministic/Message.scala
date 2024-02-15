package it.unibo.scafi.xc.simulator.deterministic

import it.unibo.scafi.xc.collections.ValueTree

protected case class Message[Id](from: Id, to: Id, content: ValueTree[String, Any]) derives CanEqual
