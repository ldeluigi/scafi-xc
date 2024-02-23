package it.unibo.scafi.xc.simulator

import it.unibo.scafi.xc.engine.context.Context

trait DiscreteSimulator[Id, Result, C <: Context[Id, ?, ?]] extends Simulator[Id, Result, C]:

  def tick(): Unit
