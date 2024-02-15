package it.unibo.scafi.xc.simulator.random

import scala.util.Random

trait RandomNumberGenerators:
  self: RandomSimulator =>
  protected val rnd: Random = Random(new java.util.Random(parameters.seed))

  protected def randomSleepTime: Int =
    randomNumber(parameters.averageSleepTime, parameters.stddevSleepTime).toInt

  protected def randomNeighborCount: Int =
    randomNumber(parameters.averageNeighbourhood, parameters.stddevNeighbourhood).toInt

  protected def isNeighbourhoodUnidirectional: Boolean =
    rnd.nextDouble() < parameters.probabilityOfOneDirectionalNeighbourhood

  protected def messageLost: Boolean =
    rnd.nextDouble() < parameters.probabilityOfMessageLoss

  protected def messageDelay: Int =
    randomNumber(parameters.averageMessageDelay, parameters.stddevMessageDelay).toInt

  protected def randomNumber(average: Double, stdev: Double): Double =
    rnd.nextGaussian() * stdev + average match
      case d if d < 0 => 0
      case d => d
end RandomNumberGenerators
