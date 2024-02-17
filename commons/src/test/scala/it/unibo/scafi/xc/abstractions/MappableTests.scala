package it.unibo.scafi.xc.abstractions

import it.unibo.scafi.xc.UnitTest

trait MappableTests:
  this: UnitTest =>
  type F[_]
  def mappable: Mappable[F]
  def toIterable[A](fa: F[A]): Iterable[A]

  def mappable(sut: F[Int]): Unit =
    given Mappable[F] = mappable
    it should "allow mapping values of collection" in:
      toIterable(summon[Mappable[F]].map(sut)(_ + 1)) should be(toIterable(sut.map(_ + 1)))
