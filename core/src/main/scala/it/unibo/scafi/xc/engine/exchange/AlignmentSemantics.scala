package it.unibo.scafi.xc.engine.exchange

import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait AlignmentSemantics:
  this: ExchangeCalculusSemantics with NValuesSemantics with StackSemantics =>

  def inboundMessages: Import[DeviceId, InvocationCoordinate, String]

  override def aligned: Set[DeviceId] = inboundMessages.view
    .filter(_._2.keys.exists(_.startsWith(currentPath)))
    .keys
    .toSet

  private def alignedMessages: Map[DeviceId, Envelope] = inboundMessages.view
    .flatMap(_._2.get(currentPath.toList))
    .toMap
