package it.unibo.scafi.xc.engine.context.common

import it.unibo.scafi.xc.engine.context.Context

trait InboundMessagesSemantics:
  this: StackSemantics & MessageSemantics & Context[DeviceId, InvocationCoordinate, Envelope] =>
  type DeviceId
  type Envelope

  protected def aligned: Set[DeviceId] = inboundMessages.view
    .filter(_._2.keys.exists(_.startsWith(currentPath)))
    .keys
    .toSet

  protected def alignedMessages: Map[DeviceId, Envelope] = inboundMessages
    .flatMap((k, v) => v.get(currentPath.toList).map(k -> _))
