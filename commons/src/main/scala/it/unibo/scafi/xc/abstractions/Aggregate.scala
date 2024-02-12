package it.unibo.scafi.xc.abstractions

trait Aggregate[F[_]]:

  extension [A](a: F[A])
    def withSelf: Iterable[A]
    def withoutSelf: Iterable[A]
