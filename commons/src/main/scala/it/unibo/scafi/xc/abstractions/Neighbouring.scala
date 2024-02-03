package it.unibo.scafi.xc.abstractions

trait Neighbouring[F[_], N[_]]:

  extension [T](f: F[T])
    def onlySelf: T
    def withoutSelf: N[T]
