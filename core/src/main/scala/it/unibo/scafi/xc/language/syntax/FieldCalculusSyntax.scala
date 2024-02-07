package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.foundation.AggregateFoundation

trait FieldCalculusSyntax:
  self: AggregateFoundation =>

  def nbr[A](expr: => A): AggregateValue[A]

  def rep[A](init: => A)(f: A => A): A

  def share[A](init: => A)(f: AggregateValue[A] => A): A
