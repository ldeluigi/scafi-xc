package it.unibo.scafi.xc.runtime

trait AggregateRuntime extends Core {
  type DeviceID
  type VM <: RoundVM
  override type Context <: BasicContext

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
  }

  protected def startRound(c: Context): VM

  @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
  protected trait ExecutionTemplate extends (Context => Export), SemanticsImplementation, AggregateProgram {
    this: Language =>
    var vm: VM = _

    override def apply(c: Context): Export = {
      vm = startRound(c)
      val result = main()
      vm.registerRoot(result)
      vm.exporting
    }
  }

  protected class LambdaInterpreter(val e: Language ?=> Any) extends ExecutionTemplate {
    this: Language =>
    given Language = this

    override def main(): Any = e
  }
}
