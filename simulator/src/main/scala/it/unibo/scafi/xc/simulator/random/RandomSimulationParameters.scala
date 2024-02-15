package it.unibo.scafi.xc.simulator.random

trait RandomSimulationParameters:
  def averageSleepTime: Double

  def stddevSleepTime: Double

  def deviceCount: Int

  def probabilityOfMessageLoss: Double

  def averageMessageDelay: Double

  def stddevMessageDelay: Double

  def averageNeighbourhood: Int

  def stddevNeighbourhood: Int

  def probabilityOfOneDirectionalNeighbourhood: Double

  def deliveredMessageLifetime: Int

  def seed: Int
end RandomSimulationParameters
