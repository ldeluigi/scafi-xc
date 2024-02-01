package it.unibo.scafi.xc.abstractions.boundaries

trait UpperBounded[T: Ordering] {
  def upperBound: T
}
