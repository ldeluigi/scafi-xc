package it.unibo.scafi.xc.simulator.deterministic

trait SleepingDevice[Id]:
  def id: Id
  def sleepTime: Int

object SleepingDevice:
  case class WithFixedSleepTime[Id](override val id: Id, override val sleepTime: Int) extends SleepingDevice[Id]
