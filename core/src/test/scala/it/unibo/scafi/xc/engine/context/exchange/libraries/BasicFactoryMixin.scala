package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.context.{ ContextFactory, ValueTreeTestingNetwork }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext

trait BasicFactoryMixin:
  type ExportValue = BasicExchangeCalculusContext.ExportValue

  val factory
      : ContextFactory[ValueTreeTestingNetwork[Int, InvocationCoordinate, Any], BasicExchangeCalculusContext[Int]] =
    n => new BasicExchangeCalculusContext[Int](n.localId, n.received)
