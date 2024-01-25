package it.unibo.scafi.xc.runtime.implementations

import it.unibo.scafi.xc.language.extensions.semantics.exchange.{ ExchangeCalculusSemantics, NValues }
import it.unibo.scafi.xc.runtime.AggregateRuntime

class ExchangeCalculusRuntime extends AggregateRuntime {
  override type VM = ExchangeVM
  override type Language = Implementation

  trait Implementation extends SemanticsImplementation, ExchangeCalculusSemantics {
    override type ID = DeviceID

    override protected def xcbranch[T](cond: NValues[DeviceID, Boolean])(th: => NValues[DeviceID, T])(
        el: => NValues[DeviceID, T],
    ): NValues[DeviceID, T] = vm.xcnest

    override protected def xcexchange[T](init: NValues[DeviceID, T])(
        f: NValues[DeviceID, T] => (NValues[DeviceID, T], NValues[DeviceID, T]),
    ): NValues[DeviceID, T] = ???

    override protected def self: DeviceID = vm.self
  }

  override def startRound(c: Context): ExchangeVM = ExchangeVM(c)

  class ExchangeVM(val context: Context) extends RoundVM {
    override def registerRoot(v: Any): Unit = ???

    override def exporting: Export = ???

    def xcnest: Nothing = ???
  }

  abstract class ExchangeCalculusInterpreter extends ExecutionTemplate with Language

  class LambdaExchangeCalculusInterpreter(val lambda: Language ?=> Any) extends LambdaInterpreter(lambda) with Language
}
