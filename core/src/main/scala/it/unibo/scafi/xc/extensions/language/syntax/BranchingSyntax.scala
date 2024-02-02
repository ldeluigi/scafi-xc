package it.unibo.scafi.xc.extensions.language.syntax

import it.unibo.scafi.xc.extensions.language.AggregateFoundation

trait BranchingSyntax[L <: AggregateFoundation]:

  extension (language: L)

    def branch[T](cond: language.AggregateValue[Boolean])(th: => language.AggregateValue[T])(
        el: => language.AggregateValue[T],
    ): language.AggregateValue[T]
