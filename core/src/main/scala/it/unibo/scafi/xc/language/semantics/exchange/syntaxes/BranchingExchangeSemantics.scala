package it.unibo.scafi.xc.language.semantics.exchange.syntaxes

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.BranchingSyntax

/**
 * This trait witnesses the fact that the exchange calculus semantics can be used to implement the branching syntax.
 */
trait BranchingExchangeSemantics extends BranchingSyntax:
  self: ExchangeCalculusSemantics =>
  override def branch[T](cond: Boolean)(th: => T)(el: => T): T = br(cond)(th)(el)
