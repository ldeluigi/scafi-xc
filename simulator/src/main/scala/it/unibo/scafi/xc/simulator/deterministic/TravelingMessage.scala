package it.unibo.scafi.xc.simulator.deterministic

protected case class TravelingMessage[Id](var delay: Int, message: Message[Id])
