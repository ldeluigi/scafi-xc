package it.unibo.scafi.xc.language.semantics.exchange.syntaxes

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.ExchangeCalculusSyntax
import it.unibo.scafi.xc.language.syntax.common.RetSend

trait ExchangeSemantics extends ExchangeCalculusSyntax:
  self: ExchangeCalculusSemantics =>

  override def exchange[T](initial: AggregateValue[T])(
      f: AggregateValue[T] => RetSend[AggregateValue[T]],
  ): AggregateValue[T] =
    xc(initial)(f.andThen(rs => (rs.ret, rs.send)))
