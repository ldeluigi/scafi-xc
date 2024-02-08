package it.unibo.scafi.xc.abstractions

trait Field[F[_] <: Iterable[_]]:
  extension [A](a: F[A]) def withoutSelf: Iterable[A]
