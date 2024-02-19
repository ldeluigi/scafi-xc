package it.unibo.scafi.xc.engine.context.common

import scala.collection.{ mutable, MapView }

import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.engine.path.Path

trait OutboundMessagesSemantics:
  this: StackSemantics & MessageSemantics & Context[DeviceId, InvocationCoordinate, Envelope] =>
  type DeviceId
  type Envelope

  override def outboundMessages: Export[DeviceId, InvocationCoordinate, Envelope] =
    var messages: Export[DeviceId, InvocationCoordinate, Envelope] =
      MapWithDefault.empty(ValueTree.empty)
    for (path, messageMap) <- sentMessages do
      for deviceId <- unalignedDevices do
        messages = messages.updated(deviceId, messages(deviceId).update(path, messageMap(deviceId)))
    messages

  private val sentMessages: mutable.Map[Path[InvocationCoordinate], MapWithDefault[DeviceId, Envelope]] =
    mutable.Map.empty

  protected def sendMessages[T](messages: MapView[DeviceId, T], default: T): Unit =
    sentMessages.update(
      currentPath.toList,
      MapWithDefault(
        messages.mapValues(close).toMap,
        close(default),
      ),
    )

  protected def unalignedDevices: Set[DeviceId]
end OutboundMessagesSemantics
