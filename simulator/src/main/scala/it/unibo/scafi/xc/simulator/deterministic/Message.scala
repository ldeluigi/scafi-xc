package it.unibo.scafi.xc.simulator.deterministic

import it.unibo.scafi.xc.collections.ValueTree

protected case class Message[Id, Token, Value](from: Id, to: Id, content: ValueTree[Token, Value]) derives CanEqual
