package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }

trait FoundationWithValueFactory:
  this: AggregateFoundation with DeviceAwareAggregateFoundation =>

  def factory: AggregateValueFactory

  protected trait AggregateValueFactory extends Liftable[AggregateValue], Foldable[AggregateValue]:
    def pure[T](v: T): AggregateValue[T]

    def exclude[T](nv: AggregateValue[T])(p: DeviceID): AggregateValue[T]

    def select[T](nv: AggregateValue[T])(p: DeviceID): T

  override def lift: Liftable[AggregateValue] = factory

  override def fold: Foldable[AggregateValue] = factory

  override def convert[T]: Conversion[T, AggregateValue[T]] = factory.pure(_)

  extension [T](av: AggregateValue[T])
    override def onlySelf: T = factory.select(av)(self)
    override def withoutSelf: AggregateValue[T] = factory.exclude(av)(self)
end FoundationWithValueFactory
