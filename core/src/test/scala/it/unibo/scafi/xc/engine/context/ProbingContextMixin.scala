package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.engine.network.{ Export, Import }

trait ProbingContextMixin:

  def probe[Id, Tokens, Values, C <: Context[Id, Tokens, Values]](
      localId: Id,
      factory: ContextFactory[TestingNetwork[Id, Tokens, Values], C],
      program: C ?=> Any,
      inboundMessages: Import[Id, Tokens, Values],
  ): Export[Id, Tokens, Values] =
    val network: TestingNetwork[Id, Tokens, Values] = TestingNetwork(
      localId = localId,
      received = inboundMessages,
    )
    val probingContext: C = factory.create(network)
    program(using probingContext)
    network.send(probingContext.outboundMessages)
    network.sent

  def probe[Id, Tokens, Values, C <: Context[Id, Tokens, Values]](
      localId: Id,
      factory: ContextFactory[TestingNetwork[Id, Tokens, Values], C],
      program: C ?=> Any,
  ): Export[Id, Tokens, Values] =
    probe(localId, factory, program, Map.empty)
end ProbingContextMixin
