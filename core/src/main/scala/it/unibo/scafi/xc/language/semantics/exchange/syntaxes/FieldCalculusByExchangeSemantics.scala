package it.unibo.scafi.xc.language.semantics.exchange.syntaxes

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.{ ExchangeCalculusSyntax, FieldCalculusSyntax }

trait FieldCalculusByExchangeSemantics extends FieldCalculusSyntax:
  this: ExchangeCalculusSemantics & ExchangeCalculusSyntax =>

  override def nbr[V](expr: V): AggregateValue[V] = exchange(expr)(nv => nv)

  override def rep[A](init: A)(f: A => A): A =
    exchange[Option[A]](None)(nones =>
      val previousValue = nones(self).getOrElse(init)
      nones.set(self, Some(f(previousValue))),
    )(self).get

  override def share[A](init: A)(f: AggregateValue[A] => A): A = exchange(init)(nv => f(nv))(self)
