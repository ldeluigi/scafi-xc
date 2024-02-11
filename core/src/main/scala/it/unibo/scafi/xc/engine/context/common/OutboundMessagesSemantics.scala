package it.unibo.scafi.xc.engine.context.common

import scala.collection.{ mutable, MapView }

import it.unibo.scafi.xc.engine.network.{ Export, MessageMap }
import it.unibo.scafi.xc.engine.path.Path

trait OutboundMessagesSemantics:
  this: StackSemantics & MessageSemantics =>
  type DeviceId

  protected def outboundMessages: Export[DeviceId, InvocationCoordinate, Envelope] = sentMessages.toMap

  private val sentMessages: mutable.Map[Path[InvocationCoordinate], MessageMap[DeviceId, Envelope]] =
    mutable.Map(currentPath.toList -> MessageMap.empty(close(None)))

  protected def sendMessages[T](messages: MapView[DeviceId, T], default: T): Unit =
    sentMessages += currentPath.toList -> MessageMap(
      messages.mapValues(close).toMap,
      close(default),
    )
