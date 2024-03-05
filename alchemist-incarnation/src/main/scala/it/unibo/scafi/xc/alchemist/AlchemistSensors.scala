package it.unibo.scafi.xc.alchemist

trait AlchemistSensors:
  def sense[Value](name: String): Value
