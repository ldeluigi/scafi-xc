package it.unibo.scafi.xc.language.semantics.exchange

import scala.collection.MapView

import it.unibo.scafi.xc.abstractions.Foldable
import it.unibo.scafi.xc.language.semantics.exchange

trait NValuesWithoutSelf[DeviceId, T]:
  def default: T

  def values: MapView[DeviceId, T]

  def get(id: DeviceId): T = values.getOrElse(id, default)

  def apply(id: DeviceId): T = get(id)

object NValuesWithoutSelf:

  given [DeviceId, AV[T] <: NValuesWithoutSelf[DeviceId, T]]: Foldable[AV] with

    extension [A](a: AV[A]) override def fold[B](base: B)(acc: (B, A) => B): B = a.values.values.foldLeft(base)(acc)
