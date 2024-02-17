package it.unibo.scafi.xc.abstractions.boundaries

/**
 * Type class representing a lower bound for a type T.
 * @tparam T
 *   the type of the value
 */
trait UpperBounded[T: Ordering]:
  def upperBound: T
