package it.unibo.scafi.xc.abstractions

trait Aggregate[F[A] <: SafeIterable[A]]:

  extension [A](a: F[A]) def withoutSelf: SafeIterable[A]
