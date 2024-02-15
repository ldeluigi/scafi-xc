package it.unibo.scafi.xc.simulator.deterministic

trait Device[Id]:
  def id: Id
  def sleepTime: Int

object Device:
  case class WithFixedSleepTime[Id](override val id: Id, override val sleepTime: Int) extends Device[Id]
