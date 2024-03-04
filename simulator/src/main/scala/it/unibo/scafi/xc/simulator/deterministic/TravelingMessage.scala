package it.unibo.scafi.xc.simulator.deterministic

protected case class TravelingMessage[Id, Value](var delay: Int, message: Message[Id, Value])
