package it.unibo.scafi.xc.alchemist.main

import it.unibo.scafi.xc.alchemist.AlchemistContext
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

object TestProgram:

  def myProgram(using ExchangeCalculusSemantics): Int = rep(0)(_ + 1)

  def myProgram2(using AlchemistContext[?]): Double =
    sensorDistanceTo(self == 0)
