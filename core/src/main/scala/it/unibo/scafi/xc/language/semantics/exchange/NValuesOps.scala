package it.unibo.scafi.xc.language.semantics.exchange

import scala.collection.MapView

import it.unibo.scafi.xc.language.semantics.exchange

trait NValuesOps[NV[_], DeviceId]:

  extension [T](nv: NV[T])
    def default: T

    def values: MapView[DeviceId, T]

    def set(id: DeviceId, value: T): NV[T]

    def get(id: DeviceId): T = nv.values.getOrElse(id, nv.default)

    def apply(id: DeviceId): T = nv.get(id)
