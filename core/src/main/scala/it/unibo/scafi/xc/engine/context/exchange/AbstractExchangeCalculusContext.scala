package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.engine.context.common.*
import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

abstract class AbstractExchangeCalculusContext[Id, Wrapper](
    override val self: Id,
    override val inboundMessages: Import[Id, InvocationCoordinate, Wrapper],
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
