package it.unibo.scafi.xc.implicits.language

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }

trait AggregateFoundation[AV[_]] {
  type AggregateValue[T] = AV[T]

  /**
   * Aggregate values can be composed/mapped into new aggregate values.
   */
  given lift: Liftable[AV]

  /**
   * Aggregate values can be folded over.
   */
  given fold: Foldable[AV]

  /**
   * Local values can be considered aggregate values.
   * @tparam T
   *   can be any local value
   */
  given convert[T]: Conversion[T, AV[T]]

  // Default builtins
  extension [T](av: AV[T]) {

    /**
     * Restricts the aggregate value scope to "self".
     */
    def onlySelf: T

    /**
     * Restricts the aggregate value scope to "others".
     */
    def withoutSelf: AV[T]

    /**
     * Folds over the aggregate value, ignoring the "self" value.
     * @param base
     *   the base value
     * @param acc
     *   the accumulator function
     * @tparam B
     *   the type of the base value
     * @return
     *   the folded value
     */
    def nfold[B](base: B)(acc: (B, T) => B): B =
      av.withoutSelf.fold(base)(acc)
  }
}
