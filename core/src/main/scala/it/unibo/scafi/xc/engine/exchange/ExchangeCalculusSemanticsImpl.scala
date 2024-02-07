package it.unibo.scafi.xc.engine.exchange

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait ExchangeCalculusSemanticsImpl:
  this: ExchangeCalculusSemantics & NValuesSemantics & StackSemantics & AlignmentSemantics &
    OutboundMessagesSemantics =>

  override protected def br[T](cond: Boolean)(th: => T)(el: => T): T = scope(s"branch/$cond"): () =>
    if cond then th else el

  override protected def xc[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T] = scope("exchange"): () =>
    val messages = alignedMessages.map((k, v) => (k, open[T](v)))
    val subject = NValuesImpl[T](init.onlySelf, messages)
    val (ret, send) = f(subject)
    sendMessages += currentPath.toList -> Map.WithDefault(
      send.alignedValues.mapValues(close).toMap,
      _ => close(send.default),
    )
    ret

end ExchangeCalculusSemanticsImpl
