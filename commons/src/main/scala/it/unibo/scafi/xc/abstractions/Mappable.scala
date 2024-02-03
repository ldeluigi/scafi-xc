package it.unibo.scafi.xc.abstractions

trait Mappable[F[_]]:
  extension [A](a: F[A]) def map[B](f: A => B): F[B]
