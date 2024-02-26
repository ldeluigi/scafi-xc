package it.unibo.scafi.xc.language.foundation

import scala.util.NotGiven
import scala.annotation.implicitNotFound

trait DistributedSystemFoundation:

  /**
   * A type class that can be used to ensure that a type is shareable. A type is shareable if it is either a primitive
   * type or a serializable type. Additionally, if a type is also marked as [[NotShareable]], it becomes not shareable.
   *
   * @tparam T
   *   the type to check
   */
  @implicitNotFound(
    "Cannot share value of type ${T}. ${T} must provide evidence for ShareableContext[${T}], and cannot be an aggregate value",
  )
  final class Shareable[T] private[DistributedSystemFoundation] (using val serializable: T <:< (AnyVal | Serializable))

  /**
   * A type class that can be used to ensure that a type does not satisfy the [[Shareable]] type class.
   *
   * @tparam T
   *   the type to check
   */
  final class NotShareable[T]

  /**
   * A type is shareable if it provides a [[ShareContext]] instance and it is not marked as [[NotShareable]].
   *
   * @param x$1
   *   a proof that the type is not [[NotShareable]]
   * @tparam T
   *   the type to check
   * @return
   *   a [[Shareable]] instance for the type
   */
  given [T <: AnyVal | Serializable](using NotGiven[NotShareable[T]]): Shareable[T] = Shareable[T]()
end DistributedSystemFoundation
