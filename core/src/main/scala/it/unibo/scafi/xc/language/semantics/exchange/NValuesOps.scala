package it.unibo.scafi.xc.language.semantics.exchange

import scala.collection.MapView

import it.unibo.scafi.xc.abstractions.Foldable
import it.unibo.scafi.xc.language.semantics.exchange

trait NValuesOps[NV[_], DeviceId]:

  extension [T](nv: NV[T])
    def default: T

    def values: MapView[DeviceId, T]

    def get(id: DeviceId): T = nv.values.getOrElse(id, nv.default)

    def apply(id: DeviceId): T = nv.get(id)

object NValuesOps:

  given [DeviceId, AV[_]](using NValuesOps[AV, DeviceId]): Foldable[AV] with

    extension [A](a: AV[A]) override def fold[B](base: B)(acc: (B, A) => B): B = a.values.values.foldLeft(base)(acc)
