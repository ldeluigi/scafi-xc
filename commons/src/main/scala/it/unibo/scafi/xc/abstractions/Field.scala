package it.unibo.scafi.xc.abstractions

trait Field[F[T] <: Iterable[T]]:
  extension [A](a: F[A]) def withoutSelf: Iterable[A]
