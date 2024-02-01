package it.unibo.scafi.xc.language.semantics.exchange.syntaxes

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

trait XCSupportsFC extends FieldCalculusSyntax { self: ExchangeCalculusSemantics =>
  override def nbr[V](expr: => AggregateValue[V]): AggregateValue[V] = ???

  override def rep[A](init: => AggregateValue[A])(f: AggregateValue[A] => AggregateValue[A]): AggregateValue[A] = ???

  override def share[A](init: => AggregateValue[A])(f: AggregateValue[A] => AggregateValue[A]): AggregateValue[A] = ???
}
