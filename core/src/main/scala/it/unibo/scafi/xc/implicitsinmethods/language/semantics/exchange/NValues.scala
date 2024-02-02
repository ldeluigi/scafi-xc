package it.unibo.scafi.xc.implicitsinmethods.language.semantics.exchange

import scala.collection.MapView

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }

case class NValues[ID, +V](default: V, values: MapView[ID, V]):
  def apply(id: ID): V = values.getOrElse(id, default)

object NValues:

  given [ID, V]: Conversion[V, NValues[ID, V]] with

    def apply(v: V): NValues[ID, V] = NValues[ID, V](default = v, values = MapView.empty)

  given [ID]: Liftable[[V] =>> NValues[ID, V]] with

    extension [A](a: NValues[ID, A])

      override def map[B](f: A => B): NValues[ID, B] =
        NValues[ID, B](default = f(a.default), values = a.values.mapValues(f))

    override def lift[A, B, C](a: NValues[ID, A], b: NValues[ID, B])(f: (A, B) => C): NValues[ID, C] =
      NValues[ID, C](
        default = f(a.default, b.default),
        values = (a.values.keySet ++ b.values.keySet).map { id =>
          id -> f(a(id), b(id))
        }.toMap.view,
      )

    override def lift[A, B, C, D](a: NValues[ID, A], b: NValues[ID, B], c: NValues[ID, C])(
        f: (A, B, C) => D,
    ): NValues[ID, D] =
      NValues[ID, D](
        default = f(a.default, b.default, c.default),
        values = (a.values.keySet ++ b.values.keySet ++ c.values.keySet).map { id =>
          id -> f(a(id), b(id), c(id))
        }.toMap.view,
      )

  end given

  given [ID]: Foldable[[V] =>> NValues[ID, V]] with

    extension [A](a: NValues[ID, A])

      override def fold[B](base: B)(acc: (B, A) => B): B =
        a.values.values.foldLeft(base)(acc)
end NValues
