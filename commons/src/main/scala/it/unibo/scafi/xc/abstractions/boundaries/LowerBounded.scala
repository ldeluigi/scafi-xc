package it.unibo.scafi.xc.abstractions.boundaries

trait LowerBounded[T] extends Ordering[T] {
  def lowerBound: T
}
