package it.unibo.scafi.xc.abstractions

trait Neighbouring[F[T] <: N[T], N[_]]:

  extension [T](f: F[T])
    def onlySelf: T
    def withoutSelf: N[T]
