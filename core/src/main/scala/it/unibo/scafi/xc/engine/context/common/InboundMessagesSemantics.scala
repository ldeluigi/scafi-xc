package it.unibo.scafi.xc.engine.context.common

import it.unibo.scafi.xc.engine.network.Import

trait InboundMessagesSemantics:
  this: StackSemantics & MessageSemantics =>
  type DeviceId

  protected def inboundMessages: Import[DeviceId, InvocationCoordinate, Envelope]

  protected def aligned: Set[DeviceId] = inboundMessages.view
    .filter(_._2.keys.exists(_.startsWith(currentPath)))
    .keys
    .toSet

  protected def alignedMessages: Map[DeviceId, Envelope] = inboundMessages
    .flatMap((k, v) => v.get(currentPath.toList).map(k -> _))
