package it.unibo.scafi.xc.implicits.language.syntax

import it.unibo.scafi.xc.implicits.language.AggregateFoundation

trait BranchingSyntax[L <: AggregateFoundation] {

  def branch[T](using language: L)(cond: language.AggregateValue[Boolean])(th: => language.AggregateValue[T])(
      el: => language.AggregateValue[T],
  ): language.AggregateValue[T]
}
