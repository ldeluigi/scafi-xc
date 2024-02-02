package it.unibo.scafi.xc.cake.language.syntax

import it.unibo.scafi.xc.cake.language.AggregateFoundation

trait BranchingSyntax:
  self: AggregateFoundation =>
  def branch[T](cond: AggregateValue[Boolean])(th: => AggregateValue[T])(el: => AggregateValue[T]): AggregateValue[T]
