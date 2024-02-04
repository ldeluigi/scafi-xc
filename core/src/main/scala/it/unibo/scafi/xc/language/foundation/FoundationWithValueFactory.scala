package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.abstractions.Neighbouring

trait FoundationWithValueFactory:
  this: AggregateFoundation with DeviceAwareAggregateFoundation =>

  def factory: AggregateValueFactory

  protected trait AggregateValueFactory extends Neighbouring[AggregateValue, NeighbouringValue]:
    def pure[T](v: T): AggregateValue[T]

    def exclude[T](nv: AggregateValue[T])(p: DeviceId): NeighbouringValue[T]

    def select[T](nv: AggregateValue[T])(p: DeviceId): T

    extension [T](av: AggregateValue[T])
      override def onlySelf: T = factory.select(av)(self)
      override def withoutSelf: NeighbouringValue[T] = factory.exclude(av)(self)

  override def convert[T]: Conversion[T, AggregateValue[T]] = factory.pure(_)

  override def neighbouring: Neighbouring[AggregateValue, NeighbouringValue] = factory
end FoundationWithValueFactory
