package it.unibo.scafi.xc.simulator

import it.unibo.scafi.xc.engine.context.Context

trait DiscreteSimulator[Id, C <: Context[Id, ?, ?]] extends Simulator[Id, C]:

  def tick(): Unit
