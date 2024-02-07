package it.unibo.scafi.xc.engine.exchange

import scala.collection.mutable

import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.engine.path.Path
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait OutboundMessagesSemantics:
  this: ExchangeCalculusSemantics & StackSemantics =>

  def outboundMessages: Export[DeviceId, InvocationCoordinate, Envelope] = sendMessages.toMap

  protected val sendMessages: mutable.Map[Path[InvocationCoordinate], Map.WithDefault[DeviceId, Envelope]] =
    mutable.Map.empty
