package it.unibo.scafi.xc.tests

import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.simulator.DiscreteSimulator
import it.unibo.scafi.xc.simulator.deterministic.{ DeterministicSimulator, Device }

trait SimpleSimulatorBasedTest extends DiscreteSimulatorBasedTest:
  override type C <: Context[DeviceId, InvocationCoordinate, Any]
  given idEquality: CanEqual[DeviceId, DeviceId] = CanEqual.derived

  def network: Map[Device[DeviceId], Set[DeviceId]]

  def program(using C): Unit

  def contextFactory: ContextFactory[DeterministicSimulator.SimulatedNetwork[DeviceId], C]

  override def simulator: DiscreteSimulator[DeviceId, C] =
    val neighbourhoods: Map[Device[DeviceId], Set[DeviceId]] = network
    DeterministicSimulator[DeviceId, C](
      contextFactory = contextFactory,
      program = program,
      devices = neighbourhoods.keys.toList,
      deviceNeighbourhood = neighbourhoods.map(_.id -> _),
      deliveredMessageLifetime = neighbourhoods.keys.map(_.sleepTime).max + 1,
    )
end SimpleSimulatorBasedTest
