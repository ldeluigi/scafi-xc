package it.unibo.scafi.xc.simulator

import it.unibo.scafi.xc.engine.context.Context

trait Simulator[Id, Result, C <: Context[Id, ?, ?]]:
  def program: C ?=> Result

  def deviceNeighbourhood: Map[Id, Set[Id]]

  def results: Map[Id, Result]
