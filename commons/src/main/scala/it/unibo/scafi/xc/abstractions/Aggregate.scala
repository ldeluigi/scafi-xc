package it.unibo.scafi.xc.abstractions

import it.unibo.scafi.xc.collections.SafeIterable

trait Aggregate[F[A] <: SafeIterable[A]]:

  extension [A](a: F[A])
    def withoutSelf: SafeIterable[A]
    def onlySelf: A
