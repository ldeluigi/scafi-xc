package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable, Neighbouring }

trait AggregateFoundation:
  type NeighbouringValue[T]
  type AggregateValue[T] <: NeighbouringValue[T]

  /**
   * Aggregate values can be composed/mapped into new aggregate values.
   */
  given lift: Liftable[AggregateValue]
  given liftNeighbouring: Liftable[NeighbouringValue]

  /**
   * Aggregate values can be folded over.
   */
  given fold: Foldable[NeighbouringValue]

  /**
   * Local values can be considered aggregate values.
   *
   * @tparam T
   *   can be any local value
   */
  given convert[T]: Conversion[T, AggregateValue[T]]

  /**
   * Aggregate values are aware of their neighbours and their local values.
   */
  given neighbouring: Neighbouring[AggregateValue, NeighbouringValue]
end AggregateFoundation
