package it.unibo.scafi.xc.simulator

trait SimulationParameters:
  def averageSleepTime: Double

  def stdevSleepTime: Double

  def deviceCount: Int

  def messageLossProbability: Double

  def averageMessageDelay: Double

  def stdevMessageDelay: Double

  def seed: Int
