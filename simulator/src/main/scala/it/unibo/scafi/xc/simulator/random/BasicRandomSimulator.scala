package it.unibo.scafi.xc.simulator.random

import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.network.Network
import it.unibo.scafi.xc.simulator.deterministic.{ DeterministicSimulator, SleepingDevice }
import it.unibo.scafi.xc.simulator.{ deterministic, DiscreteSimulator }

class BasicRandomSimulator[Token, Value, Result, C <: Context[Int, Token, Value]](
    override val parameters: RandomSimulationParameters,
    private val contextFactory: ContextFactory[Network[Int, Token, Value], C],
    override val program: C ?=> Result,
) extends DiscreteSimulator[Int, Result, C]
    with RandomSimulator
    with RandomNumberGenerators:

  lazy val devices: List[SleepingDevice[Int]] = (0 until parameters.deviceCount)
    .map(SleepingDevice.WithFixedSleepTime(_, randomSleepTime))
    .toList

  override lazy val deviceNeighbourhood: Map[Int, Set[Int]] = initNeighbourhoods

  private lazy val delegate: DeterministicSimulator[Int, Token, Value, Result, C] = DeterministicSimulator(
    contextFactory,
    program,
    devices = devices,
    deviceNeighbourhood = deviceNeighbourhood,
    deliveredMessageLifetime = parameters.deliveredMessageLifetime,
    messageLossPolicy = _ => messageLost,
    messageDelayPolicy = _ => messageDelay,
  )

  private def initNeighbourhoods: Map[Int, Set[Int]] =
    var result: Map[Int, Set[Int]] = Map.WithDefault[Int, Set[Int]](Map.empty, Set(_))
    val deviceSet = devices.map(_.id).toSet
    val deviceWithIndex = devices.map(_.id).zipWithIndex.toMap
    for (device, i) <- deviceWithIndex do
      val neighbours = rnd
        .shuffle((deviceSet -- result(device)).toList)
        .take(randomNeighbourCount - result(device).size + 1)
        .toSet
        .union(result(device))
      result += device -> neighbours
      for neighbour <- neighbours - device do
        if deviceWithIndex(neighbour) > i && !isNeighbourhoodUnidirectional then
          result += neighbour -> (result(neighbour) + device)
    result

  export delegate.{ results, tick }
end BasicRandomSimulator
