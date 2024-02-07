package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.foundation.AggregateFoundation

trait FieldCalculusSyntax:
  self: AggregateFoundation =>

  def nbr[A](expr: => AggregateValue[A]): AggregateValue[A]

  def rep[A](init: => AggregateValue[A])(f: AggregateValue[A] => AggregateValue[A]): AggregateValue[A]

  def share[A](init: => AggregateValue[A])(f: AggregateValue[A] => AggregateValue[A]): AggregateValue[A]
