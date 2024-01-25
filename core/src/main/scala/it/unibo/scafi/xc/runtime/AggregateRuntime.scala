package it.unibo.scafi.xc.runtime

trait AggregateRuntime extends Core {
  type DeviceID
  type VM <: RoundVM
  override type Context <: BasicContext

  def factory: AbstractFactory

  protected trait AbstractFactory {
    def createExport: Export
  }

  protected trait BasicContext {
    def selfId: DeviceID
    def neighbors: Map[DeviceID, Export]
  }

  protected trait AggregateProgram {
    this: Language =>
    def main(): Any
  }

  protected trait SemanticsImplementation {
    this: Language =>
    def vm: VM
  }

  protected trait RoundVM {
    def registerRoot(v: Any): Unit
    def context: Context
    def self: DeviceID = context.selfId
    def exporting: Export
    def alignedNeighbors: List[DeviceID]
  }

  protected def startRound(c: Context): VM

  @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
  trait ExecutionTemplate extends (Context => Export), SemanticsImplementation, AggregateProgram {
    this: Language =>
    var vm: VM = _

    override def apply(c: Context): Export = {
      vm = startRound(c)
      val result = main()
      vm.registerRoot(result)
      vm.exporting
    }
  }
}
