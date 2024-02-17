package it.unibo.scafi.xc.abstractions.boundaries

/**
 * Type class representing a bounded type.
 * @tparam T
 *   the type of the value
 */
trait LowerBounded[T: Ordering]:
  def lowerBound: T
