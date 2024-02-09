package it.unibo.scafi.xc.simulator.basic

import scala.util.Random

import it.unibo.scafi.xc.abstractions.BidirectionalFunction.<=>
import it.unibo.scafi.xc.engine.BaseEngine
import it.unibo.scafi.xc.engine.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.exchange.ExchangeCalculusContext
import it.unibo.scafi.xc.engine.network._
import it.unibo.scafi.xc.simulator.{ SimulationParameters, Simulator }

class BasicSimulator(
    parameters: SimulationParameters,
    private val program: ExchangeCalculusContext[Int] ?=> Any,
) extends Simulator(parameters):
  private val SEPARATOR = "/"

  private type DeviceId = Int
  private val rnd: Random = Random(new java.util.Random(parameters.seed))
//  private var messageQueue: Queue[TravelingMessage] = Queue.empty
//  private var deliveredMessages: Queue[DeliveredMessage] = Queue.empty

  private val devicePool: List[Device] = (0 until parameters.deviceCount)
    .map(Device(_, program, randomSleepTime()))
    .toList

  private def randomSleepTime(): Int =
    randomNumber(parameters.averageSleepTime, parameters.stdevSleepTime).toInt

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

  private class Device(
      private val id: DeviceId,
      private val program: ExchangeCalculusContext[DeviceId] ?=> Any,
      private val sleepTime: Int,
  ):

    private var slept = 0

    private val engine = BaseEngine[DeviceId, Any, InvocationCoordinate, Any, ExchangeCalculusContext[DeviceId]](
      net = network(id),
      factory = ExchangeCalculusContext(_, _),
      program,
    )

    def fire(): Any =
      if slept >= sleepTime then
        slept = 0
        engine.cycle()
      else slept += 1
  end Device

//  private case class TravelingMessage()

//  private case class DeliveredMessage()

  private class BasicNetwork[DeviceId](forDevice: DeviceId) extends Network[DeviceId, String, Any]:
    override def localId: DeviceId = forDevice

    override def send(e: Export[DeviceId, String, Any]): Unit = ???

    override def receive(): Import[DeviceId, String, Any] = ???

  override def tick(): Unit = devicePool.foreach(_.fire())
end BasicSimulator
