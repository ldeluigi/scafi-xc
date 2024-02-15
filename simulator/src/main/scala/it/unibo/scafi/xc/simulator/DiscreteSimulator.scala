package it.unibo.scafi.xc.simulator

import it.unibo.scafi.xc.engine.context.Context

trait DiscreteSimulator[C <: Context[?, ?, ?]]:
  def program: C ?=> Any

  def tick(): Unit
