package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.foundation.AggregateFoundation

trait FieldCalculusSyntax:
  self: AggregateFoundation =>

  def nbr[V](expr: => AggregateValue[V]): AggregateValue[V]

  def rep[A](init: => AggregateValue[A])(f: AggregateValue[A] => AggregateValue[A]): AggregateValue[A]

  def share[A](init: => AggregateValue[A])(f: AggregateValue[A] => AggregateValue[A]): AggregateValue[A]
