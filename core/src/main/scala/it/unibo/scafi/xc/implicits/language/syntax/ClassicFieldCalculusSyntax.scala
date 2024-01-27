package it.unibo.scafi.xc.implicits.language.syntax

import it.unibo.scafi.xc.implicits.language.AggregateFoundation

trait ClassicFieldCalculusSyntax[L <: AggregateFoundation] {
  def nbr[V](using language: L)(expr: => language.AggregateValue[V]): language.AggregateValue[V]

  def rep[A](using L)(init: => A)(f: A => A): A

  def share[A](using L)(init: => A)(f: A => A): A
}
