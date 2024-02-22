package it.unibo.scafi.xc.simulator.deterministic

protected case class TravelingMessage[Id, Token, Value](var delay: Int, message: Message[Id, Token, Value])
