package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.AggregateFoundation

trait BranchingSyntax { self: AggregateFoundation =>
  def branch[T](cond: Boolean)(th: => T)(el: => T): T
}
