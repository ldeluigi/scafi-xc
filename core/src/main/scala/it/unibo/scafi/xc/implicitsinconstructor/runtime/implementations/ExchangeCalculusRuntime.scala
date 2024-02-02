package it.unibo.scafi.xc.implicitsinconstructor.runtime.implementations

import it.unibo.scafi.xc.implicitsinconstructor.language.semantics.exchange.{ ExchangeCalculusSemantics, NValues }
import it.unibo.scafi.xc.implicitsinconstructor.runtime.AggregateRuntime

trait ExchangeCalculusRuntime extends AggregateRuntime:
  override type Language = Implementation
  override type VM = ExchangeVM

  trait Implementation extends SemanticsImplementation, ExchangeCalculusSemantics[DeviceID]:

    override protected def xcbranch[T](cond: NValues[DeviceID, Boolean])(th: => NValues[DeviceID, T])(
        el: => NValues[DeviceID, T],
    ): NValues[DeviceID, T] = th

    override protected def xcexchange[T](init: NValues[DeviceID, T])(
        f: NValues[DeviceID, T] => (NValues[DeviceID, T], NValues[DeviceID, T]),
    ): NValues[DeviceID, T] = init

    override def self: DeviceID = vm.self

    override def neighbors: NValues[DeviceID, DeviceID] =
      NValues(self, vm.alignedNeighbors.map(i => (i, i)).toMap.view)

  override def startRound(c: Context): ExchangeVM = ExchangeVM(c)

  class ExchangeVM(val context: Context) extends RoundVM:
    override def registerRoot(v: Any): Unit = {}

    override def exporting: Export = factory.createExport

    def xcnest: Nothing = ???

    override def alignedNeighbors: List[DeviceID] = ???

  abstract class ExchangeCalculusInterpreter extends ExecutionTemplate with Language

  class ExchangeCalculusLambda(private val p: Language => Any) extends ExchangeCalculusInterpreter:
    override def main(): Any = p(this)
end ExchangeCalculusRuntime
