package it.unibo.scafi.xc.abstractions

import it.unibo.scafi.xc.UnitTest

trait MappableTests:
  this: UnitTest =>

  def mappable[F[X] <: Iterable[X]: Mappable](sut: F[Int]): Unit =
    it should "allow mapping values of collection" in:
      summon[Mappable[F]].map(sut)(_ + 1) should be(sut.map(_ + 1))
