package it.unibo.scafi.xc.cake.language.syntax

import it.unibo.scafi.xc.cake.language.AggregateFoundation

trait ExpressiveFieldCalculusSyntax:
  self: AggregateFoundation =>

  def nbr[V](expr: => AggregateValue[V]): AggregateValue[V]

  def rep[A](init: => AggregateValue[A])(f: AggregateValue[A] => AggregateValue[A]): AggregateValue[A]

  def share[A](init: => AggregateValue[A])(f: AggregateValue[A] => AggregateValue[A]): AggregateValue[A]
