package it.unibo.scafi.xc.simulator

import it.unibo.scafi.xc.engine.context.ContextFactory
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.language.libraries.CommonLibrary._
import it.unibo.scafi.xc.implementations.boundaries.CommonBoundaries.given
import it.unibo.scafi.xc.language.libraries.GradientLibrary
import it.unibo.scafi.xc.language.libraries.GradientLibrary.sensorDistanceTo
import it.unibo.scafi.xc.language.sensors.DistanceSensor
import it.unibo.scafi.xc.simulator.basic.BasicSimulator
import it.unibo.scafi.xc.simulator.basic.BasicSimulator.SimulatedNetwork

object SimulatorMain:

  private object SimulationSettings extends SimulationParameters:
    override val averageSleepTime: Double = 2
    override val stddevSleepTime: Double = 1
    override val deviceCount: Int = 10
    override val probabilityOfMessageLoss: Double = 0
    override val averageMessageDelay: Double = 3
    override val stddevMessageDelay: Double = 1
    override val averageNeighbourhood: Int = 5
    override val stddevNeighbourhood: Int = 1
    override val probabilityOfOneDirectionalNeighbourhood: Double = 0
    override val seed: Int = 42

  private def program(using c: BasicExchangeCalculusContext[Int] & DistanceSensor[Int]): Unit =
    println(
      s"${device(self)} sees ${device.withoutSelf.size} aligned neighbours with " +
        s"distance ${sensorDistanceTo(self == 0)} from source",
    )

  @main def main(): Unit =
    val sim = new BasicSimulator(
      parameters = SimulationSettings,
      contextFactory = n =>
        new BasicExchangeCalculusContext(n.localId, n.receive()) with DistanceSensor[Int]:
          override def senseDistance: AggregateValue[Int] = new NValues[Int](
            default = Int.MaxValue,
            device.unalignedValues.view.mapValues(id => if id == self then 0 else 1).toMap,
          )
      ,
      program = program,
    )
    for device <- sim.devicePool do println(s"Device ${device.id} sleeps for ${device.sleepTime} ticks")
    for (deviceId, neighbourhood) <- sim.deviceNeighbourhood do
      println(s"Device $deviceId has neighbours: ${neighbourhood.mkString(", ")}")
    for tick <- 1 to 100 do
      println(s"--------------- t_$tick ----------------")
      sim.tick()
end SimulatorMain
