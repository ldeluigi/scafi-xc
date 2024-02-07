package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }

trait AggregateFoundation:
  type NeighbouringValue[+T]
  type AggregateValue[+T] <: NeighbouringValue[T]

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
   * Aggregate values are aware of their neighbours and their local values.
   */
  extension [T](f: AggregateValue[T])
    def onlySelf: T
    def withoutSelf: NeighbouringValue[T]
end AggregateFoundation
