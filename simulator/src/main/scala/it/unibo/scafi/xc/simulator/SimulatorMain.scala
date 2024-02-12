package it.unibo.scafi.xc.simulator

import it.unibo.scafi.xc.engine.context.ContextFactory
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.simulator.basic.BasicSimulator
import it.unibo.scafi.xc.simulator.basic.BasicSimulator.SimulatedNetwork

object SimulatorMain:

  private object SimulationSettings extends SimulationParameters:
    override val averageSleepTime: Double = 2
    override val stdevSleepTime: Double = 1
    override val deviceCount: Int = 10
    override val probabilityOfMessageLoss: Double = 0
    override val averageMessageDelay: Double = 3
    override val stdevMessageDelay: Double = 1
    override val averageNeighbourhood: Int = 5
    override val stdevNeighbourhood: Int = 1
    override val probabilityOfOneDirectionalNeighbourhood: Double = 0
    override val seed: Int = 42

  private def program(using c: BasicExchangeCalculusContext[Int]): Unit =
    println(s"${c.device(c.self)} sees ${c.device.withoutSelf.size} aligned neighbours")

  @main def main(): Unit =
    val sim = new BasicSimulator(
      parameters = SimulationSettings,
      contextFactory = n => BasicExchangeCalculusContext(n.localId, n.receive()),
      program = program,
    )
    for device <- sim.devicePool do println(s"Device ${device.id} sleeps for ${device.sleepTime} ticks")
    for (deviceId, neighbourhood) <- sim.deviceNeighbourhood do
      println(s"Device $deviceId has neighbours: ${neighbourhood.mkString(", ")}")
    for tick <- 1 to 10 do
      println(s"Tick $tick")
      sim.tick()
end SimulatorMain
