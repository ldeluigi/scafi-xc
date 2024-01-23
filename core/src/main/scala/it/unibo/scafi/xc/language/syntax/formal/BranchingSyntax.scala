package it.unibo.scafi.xc.language.syntax.formal

import it.unibo.scafi.xc.language.AggregateLanguage

trait BranchingSyntax[L <: AggregateLanguage] {

  extension (language: L) {

    def branch[T](cond: language.AggregateValue[Boolean])(th: => language.AggregateValue[T])(
        el: => language.AggregateValue[T],
    ): language.AggregateValue[T]
  }
}
