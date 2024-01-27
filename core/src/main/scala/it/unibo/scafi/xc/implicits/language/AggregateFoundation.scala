package it.unibo.scafi.xc.implicits.language

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }

trait AggregateFoundation {
  type AggregateValue[T]

  /**
   * Aggregate values can be composed/mapped into new aggregate values.
   */
  given lift: Liftable[AggregateValue]

  /**
   * Aggregate values can be folded over.
   */
  given fold: Foldable[AggregateValue]

  /**
   * Local values can be considered aggregate values.
   * @tparam T
   *   can be any local value
   */
  given convert[T]: Conversion[T, AggregateValue[T]]

  // Default builtins
  extension [T](av: AggregateValue[T]) {

    /**
     * Restricts the aggregate value scope to "self".
     */
    def onlySelf: T

    /**
     * Restricts the aggregate value scope to "others".
     */
    def withoutSelf: AggregateValue[T]

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
