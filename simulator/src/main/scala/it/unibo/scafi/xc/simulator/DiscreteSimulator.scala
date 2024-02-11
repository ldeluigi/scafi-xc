package it.unibo.scafi.xc.simulator

import it.unibo.scafi.xc.engine.context.Context

trait DiscreteSimulator[C <: Context[_, _, _]]:
  def program: C ?=> Any

  def parameters: SimulationParameters

  def tick(): Unit
