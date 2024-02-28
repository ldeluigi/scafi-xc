package it.unibo.scafi.xc.alchemist.main

import it.unibo.alchemist.model.terminators.StepCount
import it.unibo.alchemist.test.EuclideanSimulationKt
import it.unibo.alchemist.model.positions.Euclidean2DPosition

@main def main(): Unit =
  val sim = EuclideanSimulationKt.loadYamlSimulation[Any, Euclidean2DPosition]("simulation.yml", java.util.Map.of())
  sim.getEnvironment.nn.addTerminator(StepCount[Any, Euclidean2DPosition](1000L))
  sim.play()
  sim.run()
