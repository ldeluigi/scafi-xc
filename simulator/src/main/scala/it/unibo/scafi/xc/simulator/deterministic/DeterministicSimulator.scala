package it.unibo.scafi.xc.simulator.deterministic

import scala.collection.mutable

import it.unibo.scafi.xc.engine.Engine
import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.network.*
import it.unibo.scafi.xc.simulator.DiscreteSimulator

class DeterministicSimulator[Id, Token, Value, C <: Context[Id, Token, Value]](
    private val contextFactory: ContextFactory[Network[Id, Token, Value], C],
    override val program: C ?=> Any,
    override val devices: List[Device[Id]],
    override val deviceNeighbourhood: Map[Id, Set[Id]],
    private val deliveredMessageLifetime: Int,
    private val messageLossPolicy: Message[Id, Token, Value] => Boolean = (_: Message[Id, Token, Value]) => false,
    private val messageDelayPolicy: Message[Id, Token, Value] => Int = (_: Message[Id, Token, Value]) => 1,
)(using CanEqual[Id, Id])
    extends DiscreteSimulator[Id, C]:

  require(
    deviceNeighbourhood.values.forall(_.subsetOf(devices.map(_.id).toSet)),
    "Invalid neighbourhood: some devices are not in the network",
  )

  private lazy val devicePool = devices.map(SimulatedDevice.apply(_))
  private val messageQueue: mutable.ListBuffer[TravelingMessage[Id, Token, Value]] = mutable.ListBuffer.empty
  private val deliveredMessages: mutable.Map[(Id, Id), DeliveredMessage[Id, Token, Value]] = mutable.Map.empty

  private case class SimulatedDevice(device: Device[Id]):
    private var slept = 0
    private var sleepTime = device.sleepTime

    private val engine = Engine[Id, Any, Token, Value, Network[Id, Token, Value], C](
      net = BasicNetwork(device.id),
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

  private class BasicNetwork(forDevice: Id) extends Network[Id, Token, Value]:
    override def localId: Id = forDevice

    override def send(e: Export[Id, Token, Value]): Unit =
      messageQueue.appendAll(
        deviceNeighbourhood(forDevice).view
          .map(id => id -> e(id))
          .map(Message(forDevice, _, _))
          .filterNot(messageLossPolicy)
          .map(m => TravelingMessage(messageDelayPolicy(m), m)),
      )

    override def receive(): Import[Id, Token, Value] = deliveredMessages.values
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
