package it.unibo.scafi.xc.language.semantics.exchange.syntaxes

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.ExchangeCalculusSyntax

trait ExchangeSemantics extends ExchangeCalculusSyntax:
  self: ExchangeCalculusSemantics =>

  override def exchange[T](initial: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]) | AggregateValue[T],
  ): AggregateValue[T] = xc(initial)(nv =>
    f(nv) match
      case retSend: AggregateValue[T] @unchecked => (retSend, retSend)
      case (ret: AggregateValue[T] @unchecked, send: AggregateValue[T] @unchecked) => (ret, send),
  )
