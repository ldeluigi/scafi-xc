package it.unibo.scafi.xc.language.extensions

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }

trait AggregateFoundation {
  type AggregateValue[T]

  given lift: Liftable[AggregateValue]

  given fold: Foldable[AggregateValue]

  given convert[T]: Conversion[T, AggregateValue[T]]

  extension [T](av: AggregateValue[T]) {
    def onlySelf: T

    def withoutSelf: AggregateValue[T]

    def nfold[B](base: B)(acc: (B, T) => B): B =
      av.withoutSelf.fold(base)(acc)
  }
}
