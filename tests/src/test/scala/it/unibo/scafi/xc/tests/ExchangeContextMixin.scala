package it.unibo.scafi.xc.tests

import it.unibo.scafi.xc.engine.context.{ Context, ContextFactory }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.engine.network.Import
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.simulator.deterministic.DeterministicSimulator.SimulatedNetwork

trait ExchangeContextMixin:
  this: SimpleSimulatorBasedTest =>
  override type DeviceId = Int

  trait TestContext extends ExchangeCalculusSemantics with Context[Int, InvocationCoordinate, Any]:
    override type DeviceId = Int

  class TestContextImpl(id: Int, messages: Import[Int, InvocationCoordinate, Any])
      extends BasicExchangeCalculusContext[Int](id, messages)
      with TestContext

  override type C = TestContext

  override def contextFactory: ContextFactory[SimulatedNetwork[Int], TestContextImpl] = n =>
    TestContextImpl(n.localId, n.receive())
