package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.engine.context.common._
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.engine.network.{ Export, Import }

abstract class AbstractExchangeCalculusContext[Id, Wrapper](
    override val self: Id,
    override protected val inboundMessages: Import[Id, InvocationCoordinate, Wrapper],
) extends Context[Id, InvocationCoordinate, Wrapper]
    with ExchangeCalculusSemantics
    with NValuesSemantics
    with ConstructsSemantics
    with StackSemantics
    with MessageSemantics
    with InboundMessagesSemantics
    with OutboundMessagesSemantics:
  override type DeviceId = Id
  override type Envelope = Wrapper

  override def messages: Export[Id, InvocationCoordinate, Wrapper] = outboundMessages
