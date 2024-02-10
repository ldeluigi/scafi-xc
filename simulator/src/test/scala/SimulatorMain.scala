import it.unibo.scafi.xc.engine.exchange.ExchangeCalculusContext
import it.unibo.scafi.xc.simulator.SimulationParameters
import it.unibo.scafi.xc.simulator.basic.BasicSimulator

object SimulatorMain:

  private object SimulationSettings extends SimulationParameters:
    override val averageSleepTime: Double = 5
    override val stdevSleepTime: Double = 2
    override val deviceCount: Int = 100
    override val probabilityOfMessageLoss: Double = 1 / 25
    override val averageMessageDelay: Double = 3
    override val stdevMessageDelay: Double = 1
    override val averageNeighbourhood: Int = 5
    override val stdevNeighbourhood: Int = 1
    override val probabilityOfOneDirectionalNeighbourhood: Double = 1 / 20
    override val seed: Int = 42

  @main def main(): Unit =
    val sim = new BasicSimulator(
      parameters = SimulationSettings,
      contextFactory = ExchangeCalculusContext(_, _),
      program = () => println("Hello, world!"),
    )
    for _ <- 1 to 10000 do sim.tick()
end SimulatorMain
