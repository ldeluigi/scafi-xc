package it.unibo.scafi.xc.implicitsinconstructor.runtime.engine

import it.unibo.scafi.xc.implicitsinconstructor.runtime.AggregateRuntime

trait Engine {
  this: AggregateRuntime =>
  override type DeviceID = Int
  override type Export = String
  override type Context = EngineContext

  class Factory extends AbstractFactory {
    override def createExport: Export = "hello"
  }

  override def factory: Factory = Factory()

  class EngineContext extends BasicContext {

    override def selfId: DeviceID = 1

    override def neighbors: Map[DeviceID, Export] = Map.empty
  }
}
