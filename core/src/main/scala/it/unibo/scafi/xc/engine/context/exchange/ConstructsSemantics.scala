package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.engine.context.common.*
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

/**
 * Implements the foundational constructs of the exchange calculus semantics.
 */
trait ConstructsSemantics:
  this: ExchangeCalculusSemantics & NValuesSemantics & MessageSemantics & StackSemantics & InboundMessagesSemantics &
    OutboundMessagesSemantics =>

  override protected def br[T](cond: Boolean)(th: => T)(el: => T): T = scope(s"branch/$cond"): () =>
    if cond then th else el

  override protected def xc[T: Shareable](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T] =
    scope("exchange"): () =>
      val messages = alignedMessages.map((k, v) => (k, open[T](v)))
      val subject = NValues[T](init(self), messages)
      val (ret, send) = f(subject)
      sendMessages(send.alignedValues, send.default)
      ret

end ConstructsSemantics
