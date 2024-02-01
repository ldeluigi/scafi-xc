package it.unibo.scafi.xc.abstractions.boundaries

trait Bounded[T: Ordering] extends LowerBounded[T], UpperBounded[T]
