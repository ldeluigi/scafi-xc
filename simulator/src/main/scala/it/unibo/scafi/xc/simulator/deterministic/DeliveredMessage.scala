package it.unibo.scafi.xc.simulator.deterministic

protected case class DeliveredMessage[Id](message: Message[Id], var lifetime: Int = 0)
