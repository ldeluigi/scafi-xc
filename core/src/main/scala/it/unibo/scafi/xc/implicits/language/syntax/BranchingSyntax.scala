package it.unibo.scafi.xc.implicits.language.syntax

import it.unibo.scafi.xc.implicits.language.AggregateFoundation

trait BranchingSyntax[AV[_], L <: AggregateFoundation[AV]](using L) {
  def branch[T](cond: AV[Boolean])(th: => AV[T])(el: => AV[T]): AV[T]
}
