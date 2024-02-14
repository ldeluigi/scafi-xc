package it.unibo.scafi.xc.engine.context.common

import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.engine.network.Import

trait InboundMessagesSemantics:
  this: StackSemantics & MessageSemantics & Context[DeviceId, InvocationCoordinate, Envelope] =>
  type DeviceId
  type Envelope

  protected def unalignedDevices: Set[DeviceId] = unalignedMessages.view.keys.toSet

  protected def alignedDevices: Set[DeviceId] = unalignedMessages.view
    .filter(currentPath.isEmpty || _._2.keys.exists(_.startsWith(currentPath)))
    .keys
    .toSet

  protected def unalignedMessages: Import[DeviceId, InvocationCoordinate, Envelope] = inboundMessages

  protected def alignedMessages: Map[DeviceId, Envelope] = unalignedMessages
    .flatMap((id, valueTree) =>
      valueTree
        .get(currentPath.toList)
        .map(id -> _),
    )
end InboundMessagesSemantics
