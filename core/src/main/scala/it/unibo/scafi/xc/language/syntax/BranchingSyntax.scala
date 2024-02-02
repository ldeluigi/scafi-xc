package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.foundation.AggregateFoundation

trait BranchingSyntax:
  self: AggregateFoundation =>
  def branch[T](cond: Boolean)(th: => T)(el: => T): T
