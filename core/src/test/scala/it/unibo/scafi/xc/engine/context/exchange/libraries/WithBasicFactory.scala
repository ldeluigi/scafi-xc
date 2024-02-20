package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.engine.context.{ ContextFactory, TestingNetwork }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext

trait WithBasicFactory:

  val factory: ContextFactory[TestingNetwork[Int, InvocationCoordinate, Any], BasicExchangeCalculusContext[Int]] =
    n => new BasicExchangeCalculusContext[Int](n.localId, n.received)
