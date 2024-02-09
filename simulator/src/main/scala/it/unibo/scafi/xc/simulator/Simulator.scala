package it.unibo.scafi.xc.simulator

trait Simulator(protected val parameters: SimulationParameters):
  def tick(): Unit
