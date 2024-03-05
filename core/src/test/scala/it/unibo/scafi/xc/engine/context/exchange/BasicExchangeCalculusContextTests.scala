package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.context.ProbingContextMixin
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.libraries.*
import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.language.semantics.exchange.{
  ExchangeCalculusSemanticsTestHelper,
  ExchangeCalculusSemanticsTests,
}

class BasicExchangeCalculusContextTests
    extends UnitTest
    with ProbingContextMixin
    with BasicFactoryMixin
    with ExchangeCalculusSemanticsTests
    with BranchingTests
    with ExchangeCalculusTests
    with FieldCalculusTests
    with FoldingTests
    with GradientTests
    with MathTests
    with FoldhoodLibraryTests:

  class BasicExchangeCalculusContextWithTestHelpers(
      self: Int,
      inboundMessages: Import[Int, ValueTree[InvocationCoordinate, Any]],
  ) extends BasicExchangeCalculusContext[Int](self, inboundMessages)
      with ExchangeCalculusSemanticsTestHelper:

    override def mockNValues[T](default: T, values: Map[DeviceId, T]): AggregateValue[T] =
      NValues(default, values)

    override def unalignedDeviceId: Int = unalignedDevices.maxOption.getOrElse(1) + 1

  given context: BasicExchangeCalculusContextWithTestHelpers = BasicExchangeCalculusContextWithTestHelpers(
    self = 0,
    inboundMessages = (0 until 10)
      .map(id =>
        id -> probe(
          localId = id,
          factory = factory,
          program = () => (),
        )(0),
      )
      .toMap,
  )

  "Basic ExchangeCalculusContext" should behave like exchangeCalculusSemanticsWithAtLeast10AlignedDevices
  "Basic ExchangeCalculusContext 'branch'" should behave like branchingSemantics()
  "Basic ExchangeCalculusContext 'exchange'" should behave like exchangeSemantics()
  "Basic ExchangeCalculusContext field calculus" should behave like fieldCalculusSemantics()
  "Basic ExchangeCalculusContext folding" should behave like foldingSemantics()
  "Basic ExchangeCalculusContext gradient library" should behave like gradientSemantics()
  "Basic ExchangeCalculusContext math library" should behave like mathLibrarySemantics()
  "Basic ExchangeCalculusContext folshood library" should behave like foldhoodSemantics()
end BasicExchangeCalculusContextTests
