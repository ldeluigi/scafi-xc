package it.unibo.scafi.xc.language.foundation

import it.unibo.scafi.xc.UnitTest

trait DeviceAwareAggregateFoundationTests:
  this: AggregateFoundationTests & UnitTest =>

  override type A <: DeviceAwareAggregateFoundation & AggregateFoundation & FieldMock & DeviceMock

  def deviceAwareAggregateFoundation(): Unit =
    it should behave like aggregateFoundation()

    it should "be able to get the device id" in:
      lang.self shouldEqual 0

    it should "provide a field of aligned neighbors" in:
      lang.device.toIterable should contain(lang.self)
