package it.unibo.scafi.xc.simulator.random

import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.simulator.{ deterministic, DiscreteSimulator }
import it.unibo.scafi.xc.simulator.deterministic.{ DeterministicSimulator, Device }

class BasicRandomSimulator[C <: Context[Int, InvocationCoordinate, Any]](
    override val parameters: RandomSimulationParameters,
    private val contextFactory: ContextFactory[DeterministicSimulator.SimulatedNetwork[Int], C],
    override val program: C ?=> Any,
) extends DiscreteSimulator[C]
    with RandomSimulator
    with RandomNumberGenerators:

  lazy val devices: List[Device[Int]] = (0 until parameters.deviceCount)
    .map(Device.WithFixedSleepTime(_, randomSleepTime))
    .toList

  lazy val neighborhoods: Map[Int, Set[Int]] = initNeighborhoods

  lazy val delegate: DeterministicSimulator[Int, C] = DeterministicSimulator(
    contextFactory,
    program,
    devices = devices,
    deviceNeighbourhood = neighborhoods,
    deliveredMessageLifetime = parameters.deliveredMessageLifetime,
    messageLossPolicy = _ => messageLost,
    messageDelayPolicy = _ => messageDelay,
  )

  private def initNeighborhoods: Map[Int, Set[Int]] =
    var result: Map[Int, Set[Int]] = Map.WithDefault[Int, Set[Int]](Map.empty, Set(_))
    val deviceSet = devices.map(_.id).toSet
    val deviceWithIndex = devices.map(_.id).zipWithIndex.toMap
    for (device, i) <- deviceWithIndex do
      val neighbours = rnd
        .shuffle((deviceSet -- result(device)).toList)
        .take(randomNeighborCount - result(device).size + 1)
        .toSet
        .union(result(device))
      result += device -> neighbours
      for neighbour <- neighbours - device do
        if deviceWithIndex(neighbour) > i && !isNeighbourhoodUnidirectional then
          result += neighbour -> (result(neighbour) + device)
    result

  override def tick(): Unit = delegate.tick()
end BasicRandomSimulator
