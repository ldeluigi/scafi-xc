package it.unibo.scafi.xc.implementations.boundaries

import it.unibo.scafi.xc.abstractions.boundaries.Bounded

object CommonBoundaries:

  given Bounded[Double] with
    override def lowerBound: Double = Double.NegativeInfinity
    override def upperBound: Double = Double.PositiveInfinity

  given Bounded[Float] with
    override def lowerBound: Float = Float.NegativeInfinity
    override def upperBound: Float = Float.PositiveInfinity
