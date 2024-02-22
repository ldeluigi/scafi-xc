package it.unibo.scafi.xc.tests

import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.engine.network.{ Import, Network }
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait ExchangeContextMixin:
  this: SimpleSimulatorBasedTest =>
  override type DeviceId = Int

  trait TestContext extends ExchangeCalculusSemantics with Context[Int, InvocationCoordinate, Any]:
    override type DeviceId = Int

  class TestContextImpl(id: Int, messages: Import[Int, InvocationCoordinate, Any])
      extends BasicExchangeCalculusContext[Int](id, messages)
      with TestContext

  override type C = TestContext

  override def contextFactory: ContextFactory[Network[Int, InvocationCoordinate, Any], TestContextImpl] = n =>
    TestContextImpl(n.localId, n.receive())
