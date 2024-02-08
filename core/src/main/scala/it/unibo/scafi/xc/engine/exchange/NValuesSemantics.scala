package it.unibo.scafi.xc.engine.exchange

import scala.collection.MapView

import it.unibo.scafi.xc.abstractions.{ Field, Liftable }
import it.unibo.scafi.xc.language.semantics.exchange.{ ExchangeCalculusSemantics, NValuesOps }

trait NValuesSemantics:
  this: ExchangeCalculusSemantics =>
  override type AggregateValue[T] = NValuesImpl[T]

  protected case class NValuesImpl[+T](default: T, unalignedValues: Map[DeviceId, T] = Map.empty) extends Iterable[T]:
    def alignedValues: MapView[DeviceId, T] = unalignedValues.view.filterKeys(aligned)
    override def iterator: Iterator[T] = unalignedValues.valuesIterator
    def apply(id: DeviceId): T = alignedValues(id)

  override given nvalues: NValuesOps[AggregateValue, DeviceId] = new NValuesOps[AggregateValue, DeviceId]:
    extension [T](nv: AggregateValue[T]) override def default: T = nv.default
    extension [T](nv: AggregateValue[T]) override def values: MapView[DeviceId, T] = nv.alignedValues

    extension [T](nv: AggregateValue[T])

      override def set(id: DeviceId, value: T): AggregateValue[T] = new NValuesImpl[T](
        nv.default,
        nv.unalignedValues + (id -> value),
      )

  override given liftable: Liftable[AggregateValue] = new Liftable[AggregateValue]:

    override def lift[A, B](a: NValuesImpl[A])(f: A => B): NValuesImpl[B] =
      new NValuesImpl[B](f(a.default), a.unalignedValues.view.mapValues(f).toMap)

    override def lift[A, B, C](a: NValuesImpl[A], b: NValuesImpl[B])(f: (A, B) => C): NValuesImpl[C] =
      new NValuesImpl[C](
        f(a.default, b.default),
        (a.unalignedValues.keySet ++ b.unalignedValues.keySet).map(k => k -> f(a(k), b(k))).toMap,
      )

    override def lift[A, B, C, D](a: NValuesImpl[A], b: NValuesImpl[B], c: NValuesImpl[C])(
        f: (A, B, C) => D,
    ): NValuesImpl[D] = new NValuesImpl[D](
      f(a.default, b.default, c.default),
      (a.unalignedValues.keySet ++ b.unalignedValues.keySet ++ c.unalignedValues.keySet)
        .map(k => k -> f(a(k), b(k), c(k)))
        .toMap,
    )

  override given field: Field[AggregateValue] = new Field[AggregateValue]:

    extension [A](a: AggregateValue[A])

      override def withoutSelf: Iterable[A] = new NValuesImpl[A](
        a.default,
        a.unalignedValues - self,
      )

  override given convert[T]: Conversion[T, AggregateValue[T]] = new NValuesImpl[T](_)

  override def device: AggregateValue[DeviceId] = new NValuesImpl[DeviceId](self, aligned.map(id => (id, id)).toMap)

  protected def aligned: Set[DeviceId]
end NValuesSemantics
