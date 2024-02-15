package it.unibo.scafi.xc.simulator.basic

import it.unibo.scafi.xc.collections.ValueTree

protected case class Message(from: Int, to: Int, content: ValueTree[String, Any]) derives CanEqual
