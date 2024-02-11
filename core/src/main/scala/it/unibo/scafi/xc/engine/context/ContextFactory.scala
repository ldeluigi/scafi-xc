package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.engine.network.Network

trait ContextFactory[N <: Network[_, _, _], C <: Context[_, _, _]]:
  def create(network: N): C
