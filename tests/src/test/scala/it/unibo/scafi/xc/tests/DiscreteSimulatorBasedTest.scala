package it.unibo.scafi.xc.tests

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.simulator.DiscreteSimulator
import it.unibo.scafi.xc.simulator.deterministic.Device
import org.scalatest.BeforeAndAfterEach

trait DiscreteSimulatorBasedTest extends UnitTest with BeforeAndAfterEach:
  type DeviceId
  type C <: Context[DeviceId, ?, ?]
  def simulator: DiscreteSimulator[DeviceId, C]
  def ticks: Int
  def cleanup(): Unit = ()

  override def beforeEach(): Unit =
    val sim = simulator
    for _ <- 1 to ticks do sim.tick()
    super.beforeEach()

  override def afterEach(): Unit =
    cleanup()
    super.afterEach()
