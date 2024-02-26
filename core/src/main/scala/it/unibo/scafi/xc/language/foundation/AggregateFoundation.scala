package it.unibo.scafi.xc.language.foundation

import scala.util.NotGiven
import scala.annotation.implicitNotFound

import it.unibo.scafi.xc.abstractions.{ Aggregate, Liftable }
import it.unibo.scafi.xc.collections.SafeIterable

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
   * A type class that can be used to ensure that a type is shareable. A type is shareable if it is either a primitive
   * type or a serializable type. Additionally, if a type is also marked as [[NotShareable]], it becomes not shareable.
   *
   * @tparam T
   *   the type to check
   */
  @implicitNotFound(
    "Cannot share value of type ${T}. ${T} must be either a primitive type or a serializable type, and cannot be an aggregate value",
  )
  final class Shareable[T] private[AggregateFoundation] ()

  /**
   * A type class that can be used to ensure that a type does not satisfy the [[Shareable]] type class.
   *
   * @tparam T
   *   the type to check
   */
  final class NotShareable[T]

  /**
   * An aggregate value is not shareable to other devices.
   * @tparam T
   *   the type of the aggregate value
   * @return
   *   a [[NotShareable]] instance for the aggregate value
   */
  given [T, A <: AggregateValue[T]]: NotShareable[A] = NotShareable[A]()

  /**
   * A type is shareable if it is a primitive type or a serializable type, and it is not marked as [[NotShareable]].
   * @param x$1
   *   a proof that the type is not [[NotShareable]]
   * @tparam T
   *   the type to check
   * @return
   *   a [[Shareable]] instance for the type
   */
  given [T <: AnyVal | Serializable](using NotGiven[NotShareable[T]]): Shareable[T] = Shareable[T]()
end AggregateFoundation
