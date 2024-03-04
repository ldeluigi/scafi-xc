package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.abstractions.{ Aggregate, Liftable }
import it.unibo.scafi.xc.collections.SafeIterable

import DistributedSystemUtilities.NotShareable

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

  /**
   * An aggregate value is not shareable to other devices.
   *
   * @tparam T
   *   the type of the aggregate value
   * @return
   *   a [[NotShareable]] instance for the aggregate value
   */
  given [T, A <: AggregateValue[T]]: NotShareable[A] = NotShareable[A]()
end AggregateFoundation
