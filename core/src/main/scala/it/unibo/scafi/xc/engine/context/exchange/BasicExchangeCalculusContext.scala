package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.engine.context.common._
import it.unibo.scafi.xc.engine.network.Import

class BasicExchangeCalculusContext[Id](
    self: Id,
    inboundMessages: Import[Id, InvocationCoordinate, Any],
) extends AbstractExchangeCalculusContext[Id, Any](self, inboundMessages)
    with MessageSemantics.Basic
