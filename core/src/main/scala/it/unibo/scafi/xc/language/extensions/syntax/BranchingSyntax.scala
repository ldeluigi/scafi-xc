package it.unibo.scafi.xc.language.extensions.syntax

import it.unibo.scafi.xc.language.extensions.AggregateFoundation

trait BranchingSyntax[L <: AggregateFoundation] {

  extension (language: L) {

    def branch[T](cond: language.AggregateValue[Boolean])(th: => language.AggregateValue[T])(
        el: => language.AggregateValue[T],
    ): language.AggregateValue[T]
  }
}
