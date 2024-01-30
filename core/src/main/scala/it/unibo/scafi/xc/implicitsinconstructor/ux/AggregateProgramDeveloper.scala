package it.unibo.scafi.xc.implicitsinconstructor.ux

import it.unibo.scafi.xc.implicitsinconstructor.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.implicitsinconstructor.runtime.engine.Engine
import it.unibo.scafi.xc.implicitsinconstructor.runtime.implementations.ExchangeCalculusRuntime

object AggregateProgramDeveloper extends ExchangeCalculusRuntime with Engine {

  // a program can either subclass an interpreter
  private class MyProgram extends ExchangeCalculusInterpreter {
    private given ExchangeCalculusSemantics[DeviceID] = this
    private val lib = ExchangeCalculusSemantics.ClassicSyntaxImpl[DeviceID]()
    import lib._

    override def main(): Any = {
      rep(1)(_ + 1)
    }
  }

  // the following tests simulate a single round of execution
  @main def test1(): Unit = {
    val p = MyProgram()
    val c: Context = EngineContext()
    println(p(c))
  }
}
