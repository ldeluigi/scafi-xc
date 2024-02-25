package it.unibo.scafi.xc.tests

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.simulator.DiscreteSimulator
import it.unibo.scafi.xc.simulator.deterministic.Device
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach }

trait DiscreteSimulatorBasedTest extends UnitTest with BeforeAndAfterAll:
  type TestDeviceId
  type TestToken
  type TestValue
  type TestProgramResult
  type TestContext <: Context[TestDeviceId, TestToken, TestValue]
  private val sim: DiscreteSimulator[TestDeviceId, TestProgramResult, TestContext] = simulator
  def simulator: DiscreteSimulator[TestDeviceId, TestProgramResult, TestContext]
  export sim.*

  def ticks: Int

  override def beforeAll(): Unit =
    for _ <- 1 to ticks do sim.tick()
    super.beforeAll()
