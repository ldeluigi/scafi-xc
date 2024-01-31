package it.unibo.scafi.xc.cake.language.syntax

import it.unibo.scafi.xc.cake.language.AggregateFoundation

trait ClassicFieldCalculusSyntax { self: AggregateFoundation =>

  def nbr[V](expr: => AggregateValue[V]): AggregateValue[V]

  def rep[A](init: => A)(f: A => A): A

  def share[A](init: => A)(f: A => A): A
}
