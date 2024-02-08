package it.unibo.scafi.xc.engine

import it.unibo.scafi.xc.engine.network.Export

trait Context[DeviceId]:
  def messages: Export[DeviceId, Any, Any]
