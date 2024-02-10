package it.unibo.scafi.xc.simulator

trait SimulationParameters:
  def averageSleepTime: Double

  def stdevSleepTime: Double

  def deviceCount: Int

  def probabilityOfMessageLoss: Double

  def averageMessageDelay: Double

  def stdevMessageDelay: Double

  def averageNeighbourhood: Int

  def stdevNeighbourhood: Int

  def probabilityOfOneDirectionalNeighbourhood: Double

  def seed: Int
end SimulationParameters
