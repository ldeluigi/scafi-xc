package it.unibo.scafi.xc.simulator.deterministic

protected case class DeliveredMessage[Id, Value](message: Message[Id, Value], var lifetime: Int = 0)
