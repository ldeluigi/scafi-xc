package it.unibo.scafi.xc.alchemist.device

import it.unibo.alchemist.model.Time
import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate

case class TimedMessage(time: Time, message: ValueTree[InvocationCoordinate, Any])
