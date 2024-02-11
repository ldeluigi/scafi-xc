package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.engine.context.common._
import it.unibo.scafi.xc.engine.network.Import

class ExchangeCalculusContext[Id](
    self: Id,
    inboundMessages: Import[Id, InvocationCoordinate, Any],
) extends AbstractExchangeCalculusContext[Id, Any](self, inboundMessages):

  override protected def open[T](a: Any): T = a match
    case t: T @unchecked => t
    case _ => throw new ClassCastException(s"Cannot cast $a to requested type")

  override protected def close[T](a: T): Any = a
