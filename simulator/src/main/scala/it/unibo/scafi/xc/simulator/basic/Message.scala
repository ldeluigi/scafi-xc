package it.unibo.scafi.xc.simulator.basic

import it.unibo.scafi.xc.engine.path.Path

protected case class Message(from: Int, to: Int, content: Map[Path[String], Any]) derives CanEqual
