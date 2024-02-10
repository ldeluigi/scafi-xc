package it.unibo.scafi.xc.simulator.basic

import scala.util.Random

import it.unibo.scafi.xc.abstractions.BidirectionalFunction.<=>
import it.unibo.scafi.xc.engine.{ Context, Engine }
import it.unibo.scafi.xc.engine.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.network.*
import it.unibo.scafi.xc.engine.path.Path
import it.unibo.scafi.xc.simulator.{ DiscreteSimulator, SimulationParameters }

class BasicSimulator[C <: Context[Int, InvocationCoordinate, Any]](
    override val parameters: SimulationParameters,
    private val contextFactory: (Int, Import[Int, InvocationCoordinate, Any]) => C,
    override val program: C ?=> Any,
) extends DiscreteSimulator[C]:
  private val SEPARATOR = "/"
  private type DeviceId = Int
  private val rnd: Random = Random(new java.util.Random(parameters.seed))
  private var messageQueue: List[TravelingMessage] = List.empty
  private var deliveredMessages: List[Message] = List.empty

  private lazy val devicePool: List[Device] = (0 until parameters.deviceCount)
    .map(Device(_, randomSleepTime))
    .toList
  private lazy val deviceNeighbourhood: Map[DeviceId, Set[DeviceId]] = initNeighborhoods

  private def initNeighborhoods: Map[DeviceId, Set[DeviceId]] =
    var result: Map[DeviceId, Set[DeviceId]] = Map.WithDefault[DeviceId, Set[DeviceId]](Map.empty, Set(_))
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

  private def randomSleepTime: Int =
    randomNumber(parameters.averageSleepTime, parameters.stdevSleepTime).toInt

  private def randomNeighborCount: Int =
    randomNumber(parameters.averageNeighbourhood, parameters.stdevNeighbourhood).toInt

  private def isNeighbourhoodUnidirectional: Boolean =
    rnd.nextDouble() < parameters.probabilityOfOneDirectionalNeighbourhood

  private def messageLost: Boolean =
    rnd.nextDouble() < parameters.probabilityOfMessageLoss

  private def messageDelay: Int =
    randomNumber(parameters.averageMessageDelay, parameters.stdevMessageDelay).toInt

  private def randomNumber(average: Double, stdev: Double): Double =
    rnd.nextGaussian() * stdev + average match
      case d if d < 0 => 0
      case d => d

  private def network(id: DeviceId): Network[DeviceId, InvocationCoordinate, Any] =
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

  private case class Device(
      id: DeviceId,
      private val sleepTime: Int,
  ):
    private var slept = 0

    private val engine = Engine[DeviceId, Any, InvocationCoordinate, Any, C](
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

  private case class Message(from: DeviceId, to: DeviceId, content: Map[Path[String], Any])

  private case class TravelingMessage(var delay: Int, message: Message)

  private class BasicNetwork(forDevice: DeviceId) extends Network[DeviceId, String, Any]:
    override def localId: DeviceId = forDevice

    override def send(e: Export[DeviceId, String, Any]): Unit =
      var messages: Map[DeviceId, Map[Path[String], Any]] = Map.WithDefault(Map.empty, _ => Map.empty)
      for (path, messageMap) <- e do
        for deviceId <- deviceNeighbourhood(forDevice) do
          messages += deviceId -> (messages(deviceId) + (path -> messageMap(deviceId)))
      messageQueue ++=
        messages
          .filter(_ => !messageLost)
          .map((deviceId, messageMap) => TravelingMessage(messageDelay, Message(forDevice, deviceId, messageMap)))

    override def receive(): Import[DeviceId, String, Any] =
      var messages: Import[DeviceId, String, Any] = Map.WithDefault(Map.empty, _ => Map.empty)
      for message <- deliveredMessages.filter(_.to == forDevice) do
        for (path, content) <- message.content do
          messages += message.from -> (messages(message.from) + (path -> content))
      deliveredMessages = deliveredMessages.filter(_.to != forDevice)
      messages

  end BasicNetwork

  override def tick(): Unit =
    for message <- messageQueue do message.delay -= 1
    deliveredMessages ++= messageQueue.filter(_.delay <= 0).map(_.message)
    messageQueue = messageQueue.filter(_.delay > 0)
    devicePool.foreach(_.fire())
end BasicSimulator
