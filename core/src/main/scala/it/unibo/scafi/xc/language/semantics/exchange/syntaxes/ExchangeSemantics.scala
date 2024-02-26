package it.unibo.scafi.xc.language.semantics.exchange.syntaxes

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.ExchangeCalculusSyntax
import it.unibo.scafi.xc.language.syntax.common.RetSend

/**
 * This trait enables the exchange syntax for the exchange calculus semantics.
 */
trait ExchangeSemantics extends ExchangeCalculusSyntax:
  self: ExchangeCalculusSemantics =>

  override def exchange[T: Shareable](initial: AggregateValue[T])(
      f: AggregateValue[T] => RetSend[AggregateValue[T]],
  ): AggregateValue[T] =
    xc(initial)(f.andThen(rs => (rs.ret, rs.send)))
