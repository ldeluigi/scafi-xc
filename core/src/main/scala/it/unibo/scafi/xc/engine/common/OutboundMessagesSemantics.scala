package it.unibo.scafi.xc.engine.common

import scala.collection.{ mutable, MapView }

import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.engine.path.Path

trait OutboundMessagesSemantics:
  this: StackSemantics & MessageSemantics =>
  type DeviceId

  def outboundMessages: Export[DeviceId, InvocationCoordinate, Envelope] = sentMessages.toMap

  private val sentMessages: mutable.Map[Path[InvocationCoordinate], Map.WithDefault[DeviceId, Envelope]] =
    mutable.Map.empty

  protected def sendMessages[T](messages: MapView[DeviceId, T], default: T): Unit =
    sentMessages += currentPath.toList -> Map.WithDefault(
      messages.mapValues(close).toMap,
      _ => close(default),
    )
