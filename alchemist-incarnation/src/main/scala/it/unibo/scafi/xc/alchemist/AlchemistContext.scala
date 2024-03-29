package it.unibo.scafi.xc.alchemist

import scala.jdk.CollectionConverters.*

import it.unibo.alchemist.model.{ Environment, Node, Position as AlchemistPosition }
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.language.sensors.DistanceSensor

class AlchemistContext[Position <: AlchemistPosition[Position]](
    environment: Environment[Any, Position],
    deviceId: Int,
    inbox: Import[Int, AlchemistContext.ExportValue],
) extends BasicExchangeCalculusContext[Int](deviceId, inbox)
    with DistanceSensor[Double]
    with AlchemistActuators
    with AlchemistSensors:

  private def me: Node[Any] = environment.getNodeByID(deviceId).nn

  @SuppressWarnings(Array("DisableSyntax.asInstanceOf"))
  override def sense[Value](name: String): Value =
    environment.getNodeByID(deviceId).nn.getConcentration(SimpleMolecule(name)).asInstanceOf[Value]

  override def update[Value](name: String, value: Value): Unit =
    environment.getNodeByID(deviceId).nn.setConcentration(SimpleMolecule(name), value)

  override def senseDistance: AggregateValue[Double] =
    val myPosition = environment.getPosition(me).nn
    val distances = environment
      .getNeighborhood(me)
      .nn
      .asScala
      .map(n =>
        n.getId ->
          environment.getPosition(n).nn.distanceTo(myPosition),
      )
      .toMap
    NValues(Double.PositiveInfinity, distances)
end AlchemistContext

object AlchemistContext:
  type ExportValue = BasicExchangeCalculusContext.ExportValue

  def sense[Value](sensor: String)(using AlchemistContext[?]): Value = summon[AlchemistContext[?]].sense(sensor)

  def update[Value](actuator: String, value: Value)(using AlchemistContext[?]): Unit =
    summon[AlchemistContext[?]].update(actuator, value)
