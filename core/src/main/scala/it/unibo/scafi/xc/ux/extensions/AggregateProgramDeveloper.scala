package it.unibo.scafi.xc.ux.extensions

import it.unibo.scafi.xc.runtime.engine.Engine
import it.unibo.scafi.xc.runtime.implementations.ExchangeCalculusRuntime

// here we decide the syntax(es) to use
import it.unibo.scafi.xc.language.extensions.semantics.exchange.ExchangeCalculusSemantics.classicSyntax

// the runtime defines the vm implementations
// while the engine defines context and exchange implementations
object AggregateProgramDeveloper extends ExchangeCalculusRuntime with Engine {

  // a program can either subclass an interpreter
  private class MyProgram extends ExchangeCalculusInterpreter {

    override def main(): Any = {
      this.rep(1)(_ + 1)
    }
  }

  // or use an instance of language
  // benefit: protected/internal methods and stuff are all hidden
  def myProgram(l: Language): Any = {
    l.rep(1)(_ + 1)
  }

  // the following tests simulate a single round of execution
  @main def test1(): Unit = {
    val p = MyProgram()
    val c: Context = EngineContext()
    println(p(c))
  }

  @main def test2(): Unit = {
    val p = ExchangeCalculusLambda(myProgram)
    val c: Context = EngineContext()
    println(p(c))
  }
}
