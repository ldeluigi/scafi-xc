package it.unibo.scafi.xc.engine.context.common

import scala.collection.{ mutable, MapView }

import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.engine.network.{ Import, MapWithDefault }
import it.unibo.scafi.xc.engine.path.{ Path, ValueTree }

trait OutboundMessagesSemantics:
  this: StackSemantics & MessageSemantics & Context[DeviceId, InvocationCoordinate, Envelope] =>
  type DeviceId
  type Envelope

  override def outboundMessages: Import[DeviceId, InvocationCoordinate, Envelope] =
    var messages: Map[DeviceId, ValueTree[InvocationCoordinate, Envelope]] = Map.empty
    for (path, messageMap) <- sentMessages do
      for deviceId <- unalignedDevices do
        messages = messages.updatedWith(deviceId)(_.map(_ + (path -> messageMap(deviceId))))
    MapWithDefault[DeviceId, ValueTree[InvocationCoordinate, Envelope]](messages, ValueTree.empty)

  private val sentMessages: mutable.Map[Path[InvocationCoordinate], MapWithDefault[DeviceId, Envelope]] =
    mutable.Map.empty

  protected def sendMessages[T](messages: MapView[DeviceId, T], default: T): Unit =
    sentMessages += currentPath.toList -> MapWithDefault(
      messages.mapValues(close).toMap,
      close(default),
    )

  protected def unalignedDevices: Set[DeviceId]
end OutboundMessagesSemantics
