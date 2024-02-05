package it.unibo.scafi.xc.engine.exchange

import scala.collection.MapView

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.language.semantics.exchange.{ ExchangeCalculusSemantics, NValuesOps }

trait NValuesSemanticsImpl:
  this: ExchangeCalculusSemantics =>
  override type AggregateValue[T] = NValuesImpl[T]
  override type NeighbouringValue[T] = NValuesImpl[T]

  protected case class NValuesImpl[T](default: T, unalignedValues: Map[DeviceId, T] = Map.empty):
    def alignedValues: MapView[DeviceId, T] = unalignedValues.view.filterKeys(aligned)

  override given nvOps: NValuesOps[NeighbouringValue, DeviceId] = new NValuesOps[NeighbouringValue, DeviceId]:
    extension [T](nv: NeighbouringValue[T]) override def default: T = nv.default
    extension [T](nv: NeighbouringValue[T]) override def values: MapView[DeviceId, T] = nv.alignedValues

  override given lift: Liftable[AggregateValue] = new Liftable[AggregateValue]:

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

  override given liftNeighbouring: Liftable[NeighbouringValue] = lift

  override given convert[T]: Conversion[T, AggregateValue[T]] = new NValuesImpl[T](_)

  extension [T](f: AggregateValue[T])

    override def withoutSelf: NeighbouringValue[T] =
      new NValuesImpl[T](f.default, f.unalignedValues - self)

  def aligned: Set[DeviceId]
end NValuesSemanticsImpl
