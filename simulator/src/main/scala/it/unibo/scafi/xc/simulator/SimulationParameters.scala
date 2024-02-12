package it.unibo.scafi.xc.simulator

trait SimulationParameters:
  def averageSleepTime: Double

  def stddevSleepTime: Double

  def deviceCount: Int

  def probabilityOfMessageLoss: Double

  def averageMessageDelay: Double

  def stddevMessageDelay: Double

  def averageNeighbourhood: Int

  def stddevNeighbourhood: Int

  def probabilityOfOneDirectionalNeighbourhood: Double

  def seed: Int
end SimulationParameters
