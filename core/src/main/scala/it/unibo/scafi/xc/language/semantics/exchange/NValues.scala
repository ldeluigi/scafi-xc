package it.unibo.scafi.xc.language.semantics.exchange

import scala.collection.MapView

import it.unibo.scafi.xc.abstractions.Liftable

trait NValues[ID, +V] {
  def default: V

  def values: MapView[ID, V]
}

object NValues {
  given [ID, V]: Conversion[V, NValues[ID, V]] with {
    def apply(v: V): NValues[ID, V] = new NValues[ID, V] {
      override def default: V = v

      override def values: MapView[ID, V] = MapView.empty
    }
  }

  given [ID]: Liftable[[V] =>> NValues[ID, V]] with {
    extension [A](a: NValues[ID, A]) override def map[B](f: A => B): NValues[ID, B] = new NValues[ID, B] {
      override def default: B = f(a.default)
      override def values: MapView[ID, B] = a.values.mapValues(f)
    }

    override def lift[A, B, C](a: NValues[ID, A], b: NValues[ID, B])(f: (A, B) => C): NValues[ID, C] = new NValues[ID, C] {
      override def default: C = f(a.default, b.default)

      override def values: MapView[ID, C] = ???
    }

    override def lift[A, B, C, D](a: NValues[ID, A], b: NValues[ID, B], c: NValues[ID, C])(f: (A, B, C) => D): NValues[ID, D] = new NValues[ID, D] {
      override def default: D = f(a.default, b.default, c.default)

      override def values: MapView[ID, D] = ???
    }
  }
}
