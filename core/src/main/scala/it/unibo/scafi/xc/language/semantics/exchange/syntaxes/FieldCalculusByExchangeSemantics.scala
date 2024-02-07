package it.unibo.scafi.xc.language.semantics.exchange.syntaxes

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.{ ExchangeCalculusSyntax, FieldCalculusSyntax }

trait FieldCalculusByExchangeSemantics extends FieldCalculusSyntax:
  self: ExchangeCalculusSemantics & ExchangeCalculusSyntax =>
  override def nbr[V](expr: => V): AggregateValue[V] = exchange(expr)(nv => nv)

  override def rep[A](init: => A)(f: A => A): A = ???

  override def share[A](init: => A)(f: AggregateValue[A] => A): A = exchange(init)(nv => f(nv)).onlySelf
