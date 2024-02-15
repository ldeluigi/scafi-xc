package it.unibo.scafi.xc.simulator.basic

import scala.collection.mutable

import it.unibo.scafi.xc.abstractions.BidirectionalFunction.<=>
import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.Engine
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.network.*
import it.unibo.scafi.xc.simulator.{ DiscreteSimulator, RandomNumberGenerators, SimulationParameters }

class BasicSimulator[C <: Context[Int, InvocationCoordinate, Any]](
    override val parameters: SimulationParameters,
    private val contextFactory: ContextFactory[BasicSimulator.SimulatedNetwork, C],
    override val program: C ?=> Any,
) extends DiscreteSimulator[C]
    with RandomNumberGenerators:
  private val SEPARATOR = "/"
  private val messageQueue: mutable.ListBuffer[TravelingMessage] = mutable.ListBuffer.empty
  private val deliveredMessages: mutable.Map[(Int, Int), DeliveredMessage] = mutable.Map.empty

  lazy val devicePool: List[Device] = (0 until parameters.deviceCount)
    .map(Device(_, randomSleepTime))
    .toList
  lazy val deviceNeighbourhood: Map[Int, Set[Int]] = initNeighborhoods

  private def initNeighborhoods: Map[Int, Set[Int]] =
    var result: Map[Int, Set[Int]] = Map.WithDefault[Int, Set[Int]](Map.empty, Set(_))
    val deviceSet = devicePool.map(_.id).toSet
    val deviceWithIndex = devicePool.map(_.id).zipWithIndex.toMap
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

  private def network(id: Int): BasicSimulator.SimulatedNetwork =
    NetworkAdapter(
      BasicNetwork(id),
      tokenAdapter = <=>[String, InvocationCoordinate](
        s =>
          s.split(SEPARATOR) match
            case Array(key, index) => InvocationCoordinate(key.nn, index.nn.toInt)
            case _ => throw new IllegalArgumentException(s"Invalid token: $s")
        ,
        id => s"${id.key}$SEPARATOR${id.index}",
      ),
      valueAdapter = <=>,
    )

  case class Device(
      id: Int,
      sleepTime: Int,
  ):
    private var slept = 0

    private val engine = Engine[Int, Any, InvocationCoordinate, Any, BasicSimulator.SimulatedNetwork, C](
      net = network(id),
      factory = contextFactory,
      program = program,
    )

    def fire(): Any =
      if slept >= sleepTime then
        slept = 0
        engine.cycle()
      else slept += 1
  end Device

  private class BasicNetwork(forDevice: Int) extends Network[Int, String, Any]:
    override def localId: Int = forDevice

    override def send(e: Export[Int, String, Any]): Unit =
      messageQueue.appendAll(
        deviceNeighbourhood(forDevice).view
          .map(id => id -> e(id))
          .filterNot(_ => messageLost)
          .map((deviceId, messageMap) => TravelingMessage(messageDelay, Message(forDevice, deviceId, messageMap))),
      )

    override def receive(): Export[Int, String, Any] =
      MapWithDefault(
        deliveredMessages.values
          .filter(_.message.to == forDevice)
          .map(m => (m.message.from, m.message.content))
          .toMap,
        ValueTree.empty,
      )

  end BasicNetwork

  override def tick(): Unit =
    for message <- messageQueue do message.delay -= 1
    for message <- deliveredMessages.values do message.lifetime += 1
    for message <- messageQueue.filter(_.delay <= 0).map(_.message) do
      deliveredMessages.update(message.from -> message.to, DeliveredMessage(message))
    deliveredMessages.filterInPlace((_, v) => v.lifetime <= parameters.deliveredMessageLifetime)
    messageQueue.filterInPlace(_.delay > 0)
    devicePool.foreach(_.fire())
end BasicSimulator

object BasicSimulator:
  type SimulatedNetwork = Network[Int, InvocationCoordinate, Any]
