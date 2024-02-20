package it.unibo.scafi.xc.simulator.deterministic

import scala.collection.mutable

import it.unibo.scafi.xc.abstractions.BidirectionalFunction.<=>
import it.unibo.scafi.xc.engine.Engine
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.network.*
import it.unibo.scafi.xc.simulator.DiscreteSimulator
import it.unibo.scafi.xc.simulator.deterministic.DeterministicSimulator.SimulatedNetwork

class DeterministicSimulator[Id, C <: Context[Id, InvocationCoordinate, Any]](
    private val contextFactory: ContextFactory[DeterministicSimulator.SimulatedNetwork[Id], C],
    override val program: C ?=> Any,
    val devices: List[Device[Id]],
    val deviceNeighbourhood: Map[Id, Set[Id]],
    val deliveredMessageLifetime: Int,
    val messageLossPolicy: Message[Id] => Boolean = (_: Message[Id]) => false,
    val messageDelayPolicy: Message[Id] => Int = (_: Message[Id]) => 1,
)(using CanEqual[Id, Id])
    extends DiscreteSimulator[C]:
  private val SEPARATOR = ":::"
  private lazy val devicePool = devices.map(SimulatedDevice.apply(_))
  private val messageQueue: mutable.ListBuffer[TravelingMessage[Id]] = mutable.ListBuffer.empty
  private val deliveredMessages: mutable.Map[(Id, Id), DeliveredMessage[Id]] = mutable.Map.empty

  private def network(id: Id): SimulatedNetwork[Id] =
    NetworkAdapter(BasicNetwork(id))
      .byToken(
        <=>(
          s =>
            s.split(SEPARATOR) match
              case Array(key, index) => InvocationCoordinate(key.nn, index.nn.toInt)
              case _ => throw new IllegalArgumentException(s"Invalid token: $s")
          ,
          id => s"${id.key}$SEPARATOR${id.index}",
        ),
      )

  private case class SimulatedDevice(device: Device[Id]):
    private var slept = 0
    private var sleepTime = device.sleepTime

    private val engine = Engine[Id, Any, InvocationCoordinate, Any, SimulatedNetwork[Id], C](
      net = network(device.id),
      factory = contextFactory,
      program = program,
    )

    def fire(): Any =
      if slept >= sleepTime then
        slept = 0
        sleepTime = device.sleepTime
        engine.cycle()
      else slept += 1
  end SimulatedDevice

  private class BasicNetwork(forDevice: Id) extends Network[Id, String, Any]:
    override def localId: Id = forDevice

    override def send(e: Export[Id, String, Any]): Unit =
      messageQueue.appendAll(
        deviceNeighbourhood(forDevice).view
          .map(id => id -> e(id))
          .map(Message(forDevice, _, _))
          .filterNot(messageLossPolicy)
          .map(m => TravelingMessage(messageDelayPolicy(m), m)),
      )

    override def receive(): Export[Id, String, Any] = deliveredMessages.values
      .filter(_.message.to == forDevice)
      .map(m => (m.message.from, m.message.content))
      .toMap
  end BasicNetwork

  override def tick(): Unit =
    for message <- messageQueue do message.delay -= 1
    for message <- deliveredMessages.values do message.lifetime += 1
    for message <- messageQueue.filter(_.delay <= 0).map(_.message) do
      deliveredMessages.update(message.from -> message.to, DeliveredMessage(message))
    deliveredMessages.filterInPlace((_, v) => v.lifetime <= deliveredMessageLifetime)
    messageQueue.filterInPlace(_.delay > 0)
    devicePool.foreach(_.fire())
end DeterministicSimulator

object DeterministicSimulator:
  type SimulatedNetwork[Id] = Network[Id, InvocationCoordinate, Any]
