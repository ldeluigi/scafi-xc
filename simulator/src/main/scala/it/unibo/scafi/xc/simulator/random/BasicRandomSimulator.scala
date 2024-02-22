package it.unibo.scafi.xc.simulator.random

import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.network.Network
import it.unibo.scafi.xc.simulator.deterministic.{ DeterministicSimulator, Device }
import it.unibo.scafi.xc.simulator.{ deterministic, DiscreteSimulator }

class BasicRandomSimulator[Token, Value, C <: Context[Int, Token, Value]](
    override val parameters: RandomSimulationParameters,
    private val contextFactory: ContextFactory[Network[Int, Token, Value], C],
    override val program: C ?=> Any,
) extends DiscreteSimulator[Int, C]
    with RandomSimulator
    with RandomNumberGenerators:

  override lazy val devices: List[Device[Int]] = (0 until parameters.deviceCount)
    .map(Device.WithFixedSleepTime(_, randomSleepTime))
    .toList

  override lazy val deviceNeighbourhood: Map[Int, Set[Int]] = initNeighbourhoods

  private lazy val delegate: DeterministicSimulator[Int, Token, Value, C] = DeterministicSimulator(
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

  override def tick(): Unit = delegate.tick()
end BasicRandomSimulator
