package it.unibo.scafi.xc.alchemist

import scala.jdk.CollectionConverters.*

import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Position as AlchemistPosition
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.language.sensors.DistanceSensor

class AlchemistContext[Position <: AlchemistPosition[Position]](
    environment: Environment[Any, Position],
    deviceId: Int,
    inbox: Import[Int, ValueTree[InvocationCoordinate, Any]],
) extends BasicExchangeCalculusContext[Int](deviceId, inbox)
    with DistanceSensor[Double]
    with AlchemistActuators
    with AlchemistSensors:

  @SuppressWarnings(Array("DisableSyntax.asInstanceOf"))
  override def sense[Value](name: String): Value =
    environment.getNodeByID(deviceId).nn.getConcentration(SimpleMolecule(name)).asInstanceOf[Value]

  override def update[Value](name: String, value: Value): Unit =
    environment.getNodeByID(deviceId).nn.setConcentration(SimpleMolecule(name), value)

  override def senseDistance: AggregateValue[Double] =
    val me = environment.getNodeByID(deviceId)
    val myPosition = environment.getPosition(me).nn
    NValues(
      Double.PositiveInfinity,
      device.toList
        .map(id => id -> environment.getPosition(environment.getNodeByID(id)).nn.distanceTo(myPosition))
        .toMap,
    )
end AlchemistContext
