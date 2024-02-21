package it.unibo.scafi.xc.simulator.random

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext

class BasicRandomSimulatorTests extends UnitTest with RandomSimulationParameters:

  override def averageSleepTime: Double = 3
  override def stddevSleepTime: Double = 2
  override def deviceCount: Int = 250
  override def probabilityOfMessageLoss: Double = 0.1
  override def averageMessageDelay: Double = 3
  override def stddevMessageDelay: Double = 2
  override def averageNeighbourhood: Int = 3
  override def stddevNeighbourhood: Int = 1
  override def probabilityOfOneDirectionalNeighbourhood: Double = 0.1
  override def deliveredMessageLifetime: Int = 10
  override def seed: Int = 1000

  "BasicRandomSimulator" should "create a randomized network according to the simulation parameters" in:
    val sut = BasicRandomSimulator[BasicExchangeCalculusContext[Int]](
      contextFactory = n => new BasicExchangeCalculusContext[Int](n.localId, n.receive()),
      parameters = this,
      program = () => (),
    )
    sut.devices.length shouldBe deviceCount
    sut.deviceNeighbourhood.map(_._2.size.toDouble).mean shouldBe averageNeighbourhood.toDouble +- 1.5
end BasicRandomSimulatorTests
