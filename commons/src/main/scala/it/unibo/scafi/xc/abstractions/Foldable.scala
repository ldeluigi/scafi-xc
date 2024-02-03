package it.unibo.scafi.xc.abstractions

trait Foldable[-F[_]]:

  extension [A](a: F[A]) def fold[B](base: B)(acc: (B, A) => B): B
