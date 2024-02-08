package it.unibo.scafi.xc.engine.exchange

import it.unibo.scafi.xc.engine.common.*
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.common.RetSend

trait ConstructsSemantics:
  this: ExchangeCalculusSemantics & NValuesSemantics & MessageSemantics & StackSemantics & InboundMessagesSemantics &
    OutboundMessagesSemantics =>

  override protected def br[T](cond: Boolean)(th: => T)(el: => T): T = scope(s"branch/$cond"): () =>
    if cond then th else el

  override protected def xc[T](init: AggregateValue[T])(
      f: AggregateValue[T] => RetSend[AggregateValue[T]],
  ): AggregateValue[T] =
    scope("exchange"): () =>
      val messages = alignedMessages.map((k, v) => (k, open[T](v)))
      val subject = NValuesImpl[T](init(self), messages)
      val returnSend = f(subject)
      sendMessages(returnSend.send.alignedValues, returnSend.send.default)
      returnSend.ret

end ConstructsSemantics
