package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.network.{ Export, Import }

trait ProbingContextMixin:

  def probe[Id, Tokens, Values, C <: Context[Id, ValueTree[Tokens, Values]]](
      localId: Id,
      factory: ContextFactory[TestingNetwork[Id, Tokens, Values], C],
      program: C ?=> Any,
      inboundMessages: Import[Id, ValueTree[Tokens, Values]],
  ): Export[Id, ValueTree[Tokens, Values]] =
    val network: TestingNetwork[Id, Tokens, Values] = TestingNetwork(
      localId = localId,
      received = inboundMessages,
    )
    val probingContext: C = factory.create(network)
    program(using probingContext)
    network.send(probingContext.outboundMessages)
    network.sent

  def probe[Id, Tokens, Values, C <: Context[Id, ValueTree[Tokens, Values]]](
      localId: Id,
      factory: ContextFactory[TestingNetwork[Id, Tokens, Values], C],
      program: C ?=> Any,
  ): Export[Id, ValueTree[Tokens, Values]] =
    probe(localId, factory, program, Map.empty)
end ProbingContextMixin
