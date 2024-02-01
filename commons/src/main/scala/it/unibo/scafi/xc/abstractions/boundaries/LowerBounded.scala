package it.unibo.scafi.xc.abstractions.boundaries

trait LowerBounded[T: Ordering] {
  def lowerBound: T
}
