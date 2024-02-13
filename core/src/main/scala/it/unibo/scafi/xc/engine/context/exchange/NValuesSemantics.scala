package it.unibo.scafi.xc.engine.context.exchange

import scala.collection.MapView
import it.unibo.scafi.xc.abstractions.{ Aggregate, Liftable, SafeIterable }
import it.unibo.scafi.xc.language.semantics.exchange.{ ExchangeCalculusSemantics, NValuesOps }

trait NValuesSemantics:
  this: ExchangeCalculusSemantics =>
  override type AggregateValue[T] = NValues[T]

  protected case class NValues[+T](default: T, unalignedValues: Map[DeviceId, T] = Map.empty) extends SafeIterable[T]:
    def alignedValues: MapView[DeviceId, T] = unalignedValues.view.filterKeys(aligned)
    def apply(id: DeviceId): T = alignedValues.getOrElse(id, default)
    override def iterator: Iterator[T] = alignedValues.values.iterator

  override given nvalues: NValuesOps[AggregateValue, DeviceId] = new NValuesOps[AggregateValue, DeviceId]:
    extension [T](nv: AggregateValue[T]) override def default: T = nv.default
    extension [T](nv: AggregateValue[T]) override def values: MapView[DeviceId, T] = nv.alignedValues

    extension [T](nv: AggregateValue[T])

      override def set(id: DeviceId, value: T): AggregateValue[T] = new NValues[T](
        nv.default,
        nv.unalignedValues + (id -> value),
      )

  override given liftable: Liftable[AggregateValue] = new Liftable[AggregateValue]:

    override def lift[A, B](a: NValues[A])(f: A => B): NValues[B] =
      new NValues[B](f(a.default), a.unalignedValues.view.mapValues(f).toMap)

    override def lift[A, B, C](a: NValues[A], b: NValues[B])(f: (A, B) => C): NValues[C] =
      new NValues[C](
        f(a.default, b.default),
        (a.unalignedValues.keySet ++ b.unalignedValues.keySet).map(k => k -> f(a(k), b(k))).toMap,
      )

    override def lift[A, B, C, D](a: NValues[A], b: NValues[B], c: NValues[C])(
        f: (A, B, C) => D,
    ): NValues[D] = new NValues[D](
      f(a.default, b.default, c.default),
      (a.unalignedValues.keySet ++ b.unalignedValues.keySet ++ c.unalignedValues.keySet)
        .map(k => k -> f(a(k), b(k), c(k)))
        .toMap,
    )

  override given aggregate: Aggregate[AggregateValue] = new Aggregate[AggregateValue]:

    extension [A](a: AggregateValue[A])
      override def withoutSelf: SafeIterable[A] = SafeIterable(a.alignedValues.filterKeys(_ != self).values)

  override given convert[T]: Conversion[T, AggregateValue[T]] = new NValues[T](_)

  override def device: AggregateValue[DeviceId] = new NValues[DeviceId](self, aligned.map(id => (id, id)).toMap)

  protected def aligned: Set[DeviceId]
end NValuesSemantics
