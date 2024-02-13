package it.unibo.scafi.xc.engine.context

import it.unibo.scafi.xc.engine.network.Network

trait ContextFactory[N <: Network[?, ?, ?], C <: Context[?, ?, ?]]:
  def create(network: N): C
