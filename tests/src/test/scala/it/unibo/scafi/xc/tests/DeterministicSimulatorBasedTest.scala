package it.unibo.scafi.xc.tests

import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.network.Network
import it.unibo.scafi.xc.simulator.DiscreteSimulator
import it.unibo.scafi.xc.simulator.deterministic.{ DeterministicSimulator, Device }

trait DeterministicSimulatorBasedTest extends DiscreteSimulatorBasedTest:
  type TestProgramContext >: TestContext
  def network: Map[Device[TestDeviceId], Set[TestDeviceId]]

  override def simulator: DeterministicSimulator[TestDeviceId, TestToken, TestValue, TestProgramResult, TestContext] =
    val neighbourhoods: Map[Device[TestDeviceId], Set[TestDeviceId]] = network
    DeterministicSimulator[TestDeviceId, TestToken, TestValue, TestProgramResult, TestContext](
      contextFactory = contextFactory,
      program = program,
      devices = neighbourhoods.keys.toList,
      deviceNeighbourhood = neighbourhoods.map(_.id -> _),
      deliveredMessageLifetime = neighbourhoods.keys.map(_.sleepTime).max + 1,
    )

  given idEquality: CanEqual[TestDeviceId, TestDeviceId] = CanEqual.derived

  def program(using TestProgramContext): TestProgramResult

  def contextFactory: ContextFactory[Network[TestDeviceId, TestToken, TestValue], TestContext]

end DeterministicSimulatorBasedTest
