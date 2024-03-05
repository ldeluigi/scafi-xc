package it.unibo.scafi.xc.tests

import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.network.Network
import it.unibo.scafi.xc.simulator.DiscreteSimulator
import it.unibo.scafi.xc.simulator.deterministic.{ DeterministicSimulator, SleepingDevice }

trait DeterministicSimulatorBasedTest extends DiscreteSimulatorBasedTest:
  type TestProgramContext >: TestContext
  def network: Map[SleepingDevice[TestDeviceId], Set[TestDeviceId]]

  override def simulator: DeterministicSimulator[TestDeviceId, TestValue, TestProgramResult, TestContext] =
    val neighbourhoods: Map[SleepingDevice[TestDeviceId], Set[TestDeviceId]] = network
    DeterministicSimulator[TestDeviceId, TestValue, TestProgramResult, TestContext](
      contextFactory = contextFactory,
      program = program,
      devices = neighbourhoods.keys.toList,
      deviceNeighbourhood = neighbourhoods.map(_.id -> _),
      deliveredMessageLifetime = neighbourhoods.keys.map(_.sleepTime).max + 1,
    )

  given idEquality: CanEqual[TestDeviceId, TestDeviceId] = CanEqual.derived

  def program(using TestProgramContext): TestProgramResult

  def contextFactory: ContextFactory[Network[TestDeviceId, TestValue], TestContext]

end DeterministicSimulatorBasedTest
