package it.unibo.scafi.xc.engine.exchange

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.language.foundation.FoundationWithValueFactory
import it.unibo.scafi.xc.language.semantics.exchange.{ ExchangeCalculusSemantics, NValues }

import scala.collection.MapView

class ExchangeContext[Id](
    override val self: Id,
    val imports: Iterable[Import[Id, String]],
    val state: Any = ???,
) extends ExchangeCalculusSemantics
    with FoundationWithValueFactory:

  override type DeviceId = Id
  override type AggregateValue[T] = NValuesImpl[T]
  override type NeighbouringValue[T] = NValuesImpl[T]

  protected case class NValuesImpl[T](default: T, unalignedValues: Map[Id, T] = Map.empty) extends NValues[Id, T]:
    override def values: MapView[Id, T] = unalignedValues.view.filterKeys(aligned)

  override given factory: AggregateValueFactory = new AggregateValueFactory:
    override def pure[T](v: T): AggregateValue[T] = new NValuesImpl[T](v)

    override def exclude[T](nv: AggregateValue[T])(p: Id): NeighbouringValue[T] =
      new NValuesImpl[T](nv.default, nv.unalignedValues.removed(p))

    override def select[T](nv: AggregateValue[T])(p: Id): T = nv(p)

  override given lift: Liftable[AggregateValue] = new Liftable[AggregateValue]:

    override def lift[A, B](a: NValuesImpl[A])(f: A => B): NValuesImpl[B] =
      new NValuesImpl[B](f(a.default), a.unalignedValues.view.mapValues(f).toMap)

    override def lift[A, B, C](a: NValuesImpl[A], b: NValuesImpl[B])(f: (A, B) => C): NValuesImpl[C] = ???

    override def lift[A, B, C, D](a: NValuesImpl[A], b: NValuesImpl[B], c: NValuesImpl[C])(
        f: (A, B, C) => D,
    ): NValuesImpl[D] = ???

  override def liftNeighbouring: Liftable[NeighbouringValue] = lift

  def aligned: Set[Id] = ???

  override protected def xc[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T] = ???

  override protected def br[T](cond: Boolean)(th: => T)(el: => T): T = ???
end ExchangeContext
