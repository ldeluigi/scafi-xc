package it.unibo.scafi.xc.alchemist.device

import it.unibo.alchemist.model.{ Position as AlchemistPosition, * }
import it.unibo.scafi.xc.engine.network.{ Export, Import, Network }
import it.unibo.scafi.xc.alchemist.TimeUtils.given Ordering[Time]

import math.Ordering.Implicits.infixOrderingOps

class ScaFiDevice[Position <: AlchemistPosition[Position], ExportValue](
    val node: Node[Any],
    val env: Environment[Any, Position],
    val retention: Time,
) extends Network[Int, ExportValue]
    with NodeProperty[Any]:
  private var inbox: Map[Int, TimedMessage[ExportValue]] = Map.empty

  private def time: Time = env.getSimulation.nn.getTime.nn // TODO: maybe it should be a public var

  override def localId: Int = node.getId

  override def send(e: Export[Int, ExportValue]): Unit =
    inbox += localId -> TimedMessage(time, e(localId))
    env
      .getNeighborhood(node)
      .nn
      .forEach: n =>
        val node: Node[Any] = n.nn
        node.asProperty(classOf[ScaFiDevice[Position, ExportValue]]).inbox += localId -> TimedMessage(
          time, // TODO: current impl uses time of the sender
          e(localId),
        )

  override def receive(): Import[Int, ExportValue] =
    inbox = inbox.filterNot(_._2.time.plus(retention) < time)
    inbox.map((id, timedMessage) => id -> timedMessage.message)

  override def getNode: Node[Any] = node

  override def cloneOnNewNode(node: Node[Any]): NodeProperty[Any] =
    ScaFiDevice(node, env, retention)
end ScaFiDevice
