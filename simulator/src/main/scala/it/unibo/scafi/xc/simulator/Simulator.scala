package it.unibo.scafi.xc.simulator

import it.unibo.scafi.xc.engine.context.Context
import it.unibo.scafi.xc.simulator.deterministic.Device

trait Simulator[Id, C <: Context[Id, ?, ?]]:
  def program: C ?=> Any

  def devices: Iterable[Device[Id]]

  def deviceNeighbourhood: Map[Id, Set[Id]]
