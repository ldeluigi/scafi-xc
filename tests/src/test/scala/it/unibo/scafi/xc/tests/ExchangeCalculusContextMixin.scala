package it.unibo.scafi.xc.tests

import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.engine.network.{ Import, Network }
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait ExchangeCalculusContextMixin:
  this: DeterministicSimulatorBasedTest =>
  override type TestToken = InvocationCoordinate
  override type TestValue = Any
  override type TestContext = BasicTestExchangeSemantics
  override type TestProgramContext = TestExchangeSemantics

  /**
   * This trait hides all the implementation details introduced by the [[BasicTestExchangeSemantics]] from the test
   * program.
   */
  trait TestExchangeSemantics extends ExchangeCalculusSemantics with Context[TestDeviceId, InvocationCoordinate, Any]:
    override type DeviceId = TestDeviceId

  class BasicTestExchangeSemantics(id: TestDeviceId, messages: Import[TestDeviceId, InvocationCoordinate, Any])
      extends BasicExchangeCalculusContext[TestDeviceId](id, messages)
      with TestExchangeSemantics

  override def contextFactory
      : ContextFactory[Network[TestDeviceId, InvocationCoordinate, Any], BasicTestExchangeSemantics] = n =>
    BasicTestExchangeSemantics(n.localId, n.receive())
end ExchangeCalculusContextMixin
