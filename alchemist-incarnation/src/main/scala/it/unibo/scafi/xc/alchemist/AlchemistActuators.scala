package it.unibo.scafi.xc.alchemist

trait AlchemistActuators:
  def update[Value](name: String, value: Value): Unit
