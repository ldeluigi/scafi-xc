package it.unibo.scafi.xc.simulator.deterministic

protected case class DeliveredMessage[Id, Token, Value](message: Message[Id, Token, Value], var lifetime: Int = 0)
