package it.unibo.scafi.xc.engine.exchange

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait OutboundMessagesSemantics:
  this: ExchangeCalculusSemantics with StackSemantics =>

  def outboundMessages: Export[DeviceId, InvocationCoordinate, String] = sendMessages.toMap

  private val sendMessages: mutable.Map[Path[InvocationCoordinate], Map.WithDefault[DeviceId, Envelope]] =
    mutable.Map.empty
