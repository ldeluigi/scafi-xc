package it.unibo.scafi.xc.simulator.basic

protected case class DeliveredMessage(message: Message, var lifetime: Int = 0)
