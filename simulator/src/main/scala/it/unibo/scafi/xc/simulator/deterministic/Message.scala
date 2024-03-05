package it.unibo.scafi.xc.simulator.deterministic

protected case class Message[Id, Value](from: Id, to: Id, content: Value) derives CanEqual
