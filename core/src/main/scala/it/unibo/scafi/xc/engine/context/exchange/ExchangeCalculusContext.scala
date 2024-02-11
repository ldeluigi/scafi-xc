package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.engine.context.common._
import it.unibo.scafi.xc.engine.network.{ Export, Import }
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

class ExchangeCalculusContext[Id](
    override val self: Id,
    override val inboundMessages: Import[Id, InvocationCoordinate, Any],
) extends Context[Id, InvocationCoordinate, Any]
    with ExchangeCalculusSemantics
    with NValuesSemantics
    with ConstructsSemantics
    with StackSemantics
    with MessageSemantics
    with InboundMessagesSemantics
    with OutboundMessagesSemantics:
  override type DeviceId = Id
  override type Envelope = Any

  override protected def open[T](a: Any): T = a match
    case t: T @unchecked => t
    case _ => throw new ClassCastException(s"Cannot cast $a to requested type")

  override protected def close[T](a: T): Any = a

  override def messages: Export[Id, InvocationCoordinate, Any] = outboundMessages
end ExchangeCalculusContext