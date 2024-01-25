package it.unibo.scafi.xc.abstractions.boundaries

trait Bounded[T] extends LowerBounded[T], UpperBounded[T]
