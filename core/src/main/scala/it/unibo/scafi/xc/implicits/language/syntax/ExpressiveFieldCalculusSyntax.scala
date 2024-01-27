package it.unibo.scafi.xc.implicits.language.syntax

import it.unibo.scafi.xc.implicits.language.AggregateFoundation

trait ExpressiveFieldCalculusSyntax[L <: AggregateFoundation] {
  def nbr[V](using language: L)(expr: => language.AggregateValue[V]): language.AggregateValue[V]

  def rep[A](using language: L)(init: => language.AggregateValue[A])(
      f: language.AggregateValue[A] => language.AggregateValue[A],
  ): language.AggregateValue[A]

  def share[A](using language: L)(init: => language.AggregateValue[A])(
      f: language.AggregateValue[A] => language.AggregateValue[A],
  ): language.AggregateValue[A]
}
