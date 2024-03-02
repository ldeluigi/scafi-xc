package it.unibo.scafi.xc.language.foundation

import scala.util.NotGiven
import scala.annotation.implicitNotFound

object DistributedSystemUtilities:

  /**
   * A type class that can be used to ensure that a type is shareable. A type is shareable if it is either a primitive
   * type or a serializable type or has been explicitly marked as shareable. Additionally, if a type is also marked as
   * [[NotShareable]], it becomes not shareable.
   *
   * @tparam T
   *   the type to check
   */
  @implicitNotFound(
    "Cannot share value of type ${T}. ${T} must be a primitive value type or a serializable type, and it must not be marked as NotShareable",
  )
  open class Shareable[T](using NotGiven[NotShareable[T]])

  /**
   * A type class that can be used to ensure that a type does not satisfy the [[Shareable]] type class.
   *
   * @tparam T
   *   the type to check
   */
  final class NotShareable[T]

  /**
   * A type is shareable if it is primitive or serializable and it is not marked as [[NotShareable]].
   *
   * @param x$1
   *   a proof that the type is not [[NotShareable]]
   * @tparam T
   *   the type to check
   * @return
   *   a [[Shareable]] instance for the type
   */
  given [T <: AnyVal | Serializable](using NotGiven[NotShareable[T]]): Shareable[T] = Shareable[T]()
end DistributedSystemUtilities
