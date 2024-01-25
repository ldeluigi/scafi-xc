package it.unibo.scafi.xc.language.extensions

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }

trait AggregateFoundation {
  type AggregateValue[T]

  // aggregate values can be composed/mapped into new aggregate values
  given lift: Liftable[AggregateValue]

  // aggregate values can be folded over
  given fold: Foldable[AggregateValue]

  // local values can be considered as aggregate value
  given convert[T]: Conversion[T, AggregateValue[T]]

  // builtins
  extension [T](av: AggregateValue[T]) {
    def onlySelf: T

    def withoutSelf: AggregateValue[T]

    def nfold[B](base: B)(acc: (B, T) => B): B =
      av.withoutSelf.fold(base)(acc)
  }
}
