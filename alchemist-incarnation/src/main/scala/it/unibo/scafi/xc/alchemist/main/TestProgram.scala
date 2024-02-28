package it.unibo.scafi.xc.alchemist.main

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.libraries.All.*

object TestProgram:

  def myProgram(using ExchangeCalculusSemantics): Int =
    val x = rep(0)(_ + 1)
    println(x)
    x
