package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.abstractions.{ Aggregate, Liftable, SafeIterable }

trait AggregateFoundation:
  type AggregateValue[T] <: SafeIterable[T]

  /**
   * Aggregate values can be iterated also by ignoring the self value.
   */
  given aggregate: Aggregate[AggregateValue]

  /**
   * Aggregate values can be composed and mapped.
   */
  given liftable: Liftable[AggregateValue]
