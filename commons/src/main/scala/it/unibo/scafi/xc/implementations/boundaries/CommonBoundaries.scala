package it.unibo.scafi.xc.implementations.boundaries

import it.unibo.scafi.xc.abstractions.boundaries.Bounded

object CommonBoundaries {

  given Bounded[Double] with {
    override def lowerBound: Double = Double.MinValue
    override def upperBound: Double = Double.MaxValue
    override def compare(x: Double, y: Double): Int = x.compareTo(y)
  }
}
