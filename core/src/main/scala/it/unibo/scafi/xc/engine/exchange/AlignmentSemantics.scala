package it.unibo.scafi.xc.engine.exchange

import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait AlignmentSemantics:
  this: ExchangeCalculusSemantics & NValuesSemantics & StackSemantics =>

  def inboundMessages: Import[DeviceId, InvocationCoordinate, Envelope]

  override def aligned: Set[DeviceId] = inboundMessages.view
    .filter(_._2.keys.exists(_.startsWith(currentPath)))
    .keys
    .toSet

  protected def alignedMessages: Map[DeviceId, Envelope] = inboundMessages
    .flatMap((k, v) => v.get(currentPath.toList).map(k -> _))
