package it.unibo.scafi.xc.abstractions.boundaries

trait UpperBounded[T] extends Ordering[T] {
  def upperBound: T
}
