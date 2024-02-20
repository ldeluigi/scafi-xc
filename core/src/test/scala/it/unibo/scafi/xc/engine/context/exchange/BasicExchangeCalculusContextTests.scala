package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.context.ProbingContextMixin
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.libraries.*
import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.language.semantics.exchange.{
  ExchangeCalculusSemanticsTestHelper,
  ExchangeCalculusSemanticsTests,
}

class BasicExchangeCalculusContextTests
    extends UnitTest
    with ProbingContextMixin
    with WithBasicFactory
    with ExchangeCalculusSemanticsTests
    with BranchingWithExchangeTests
    with ExchangeCalculusTests
    with FieldCalculusWithExchangeTests:

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
    inboundMessages = (0 until 10).map(_ -> ValueTree.empty).toMap,
  )

  "Basic ExchangeCalculusContext" should behave like exchangeCalculusSemanticsWithAtLeast10AlignedDevices
  "Basic ExchangeCalculusContext 'branch'" should behave like branchingSemantics()
  "Basic ExchangeCalculusContext 'exchange'" should behave like exchangeSemantics()
  "Basic ExchangeCalculusContext field calculus" should behave like fieldCalculusSemantics()
end BasicExchangeCalculusContextTests
