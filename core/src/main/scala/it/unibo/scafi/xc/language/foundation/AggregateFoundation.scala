package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.abstractions.{ Field, Liftable }

trait AggregateFoundation:
  type AggregateValue[T] <: Iterable[T]

  /**
   * Aggregate values can be iterated also by ignoring the self value.
   */
  given field: Field[AggregateValue]

  /**
   * Aggregate values can be composed and mapped.
   */
  given lift: Liftable[AggregateValue]
