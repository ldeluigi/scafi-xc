package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.engine.context.common.*
import it.unibo.scafi.xc.engine.context.exchange.AbstractExchangeCalculusContext.ExportValue
import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

/**
 * Mixin composition of all the semantics needed to implement the exchange calculus, except for the message semantics.
 * @param self
 *   the device id of the current device
 * @param inboundMessages
 *   inbound messages as [[Import]]
 * @tparam Id
 *   the type of the device id
 * @tparam Wrapper
 *   the type of the envelope
 */
abstract class AbstractExchangeCalculusContext[Id, Wrapper](
    override val self: Id,
    override val inboundMessages: Import[Id, ExportValue[Wrapper]],
) extends Context[Id, ExportValue[Wrapper]]
    with ExchangeCalculusSemantics
    with NValuesSemantics
    with ConstructsSemantics
    with StackSemantics
    with MessageSemantics
    with InboundMessagesSemantics
    with OutboundMessagesSemantics:
  override type DeviceId = Id
  override type Envelope = Wrapper
end AbstractExchangeCalculusContext

object AbstractExchangeCalculusContext:
  type ExportValue[Wrapper] = ValueTree[InvocationCoordinate, Wrapper]
