package it.unibo.scafi.xc.implementations.boundaries

import it.unibo.scafi.xc.abstractions.boundaries.Bounded

object CommonBoundaries {

  given Bounded[Double] with {
    override def lowerBound: Double = Double.NegativeInfinity
    override def upperBound: Double = Double.PositiveInfinity
  }

  given Bounded[Int] with {
    override def lowerBound: Int = Int.MinValue
    override def upperBound: Int = Int.MaxValue
  }

  given Bounded[Long] with {
    override def lowerBound: Long = Long.MinValue
    override def upperBound: Long = Long.MaxValue
  }

  given Bounded[Float] with {
    override def lowerBound: Float = Float.NegativeInfinity
    override def upperBound: Float = Float.PositiveInfinity
  }

  given Bounded[Short] with {
    override def lowerBound: Short = Short.MinValue
    override def upperBound: Short = Short.MaxValue
  }

  given Bounded[Byte] with {
    override def lowerBound: Byte = Byte.MinValue
    override def upperBound: Byte = Byte.MaxValue
  }

  given Bounded[Char] with {
    override def lowerBound: Char = Char.MinValue
    override def upperBound: Char = Char.MaxValue
  }
}
