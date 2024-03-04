package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.context.{ ContextFactory, TestingNetwork }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext

trait BasicFactoryMixin:

  val factory: ContextFactory[TestingNetwork[Int, InvocationCoordinate, Any], BasicExchangeCalculusContext[Int]] =
    n => new BasicExchangeCalculusContext[Int](n.localId, n.received)
