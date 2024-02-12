package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.abstractions.{ Aggregate, Liftable }

trait AggregateFoundation:
  type AggregateValue[T]

  /**
   * Aggregate values can be iterated also by ignoring the self value.
   */
  given aggregate: Aggregate[AggregateValue]

  /**
   * Aggregate values can be composed and mapped.
   */
  given liftable: Liftable[AggregateValue]
