package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.engine.context.common._
import it.unibo.scafi.xc.engine.network.Export

class BasicExchangeCalculusContext[Id](
    self: Id,
    inboundMessages: Export[Id, InvocationCoordinate, Any],
) extends AbstractExchangeCalculusContext[Id, Any](self, inboundMessages)
    with MessageSemantics.Basic
