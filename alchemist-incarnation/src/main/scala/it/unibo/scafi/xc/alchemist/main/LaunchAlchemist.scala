package it.unibo.scafi.xc.alchemist.main

import it.unibo.alchemist.model.terminators.AfterTime
import it.unibo.alchemist.test.EuclideanSimulationKt
import it.unibo.alchemist.model.positions.Euclidean2DPosition
import it.unibo.alchemist.model.times.DoubleTime

@main def main(): Unit =
  val sim = EuclideanSimulationKt.loadYamlSimulation[Any, Euclidean2DPosition]("simulation.yml", java.util.Map.of())
  sim.getEnvironment.nn.addTerminator(AfterTime[Any, Euclidean2DPosition](DoubleTime(1000.0)))
  sim.play()
  sim.run()
