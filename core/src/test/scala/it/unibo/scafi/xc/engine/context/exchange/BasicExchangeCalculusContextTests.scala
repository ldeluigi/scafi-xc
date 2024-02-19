package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.language.semantics.exchange.{
  ExchangeCalculusSemanticsTestHelper,
  ExchangeCalculusSemanticsTests,
}

class BasicExchangeCalculusContextTests extends UnitTest with ExchangeCalculusSemanticsTests:

  class BasicExchangeCalculusContextWithTestHelpers(
      self: Int,
      inboundMessages: Export[Int, InvocationCoordinate, Any],
  ) extends BasicExchangeCalculusContext[Int](self, inboundMessages)
      with ExchangeCalculusSemanticsTestHelper:

    override def mockNValues[T](default: T, values: Map[DeviceId, T]): AggregateValue[T] =
      NValues(default, values)

    override def unalignedDeviceId: Int = unalignedDevices.maxOption.getOrElse(1) + 1

  given context: BasicExchangeCalculusContextWithTestHelpers = BasicExchangeCalculusContextWithTestHelpers(
    self = 0,
    inboundMessages = MapWithDefault(
      (0 until 10).map(_ -> ValueTree.empty).toMap,
      default = ValueTree.empty,
    ),
  )

  "Basic ExchangeCalculusContext" should behave like exchangeCalculusSemanticsWithAtLeast10AlignedDevices
end BasicExchangeCalculusContextTests
