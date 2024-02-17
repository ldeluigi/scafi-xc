package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.language.foundation.{ AggregateFoundation, AggregateFoundationMock }

class BranchingSyntaxTests extends UnitTest:

  val language: BranchingSyntax & AggregateFoundation = new AggregateFoundationMock with BranchingSyntax:
    override def branch[T](cond: Boolean)(th: => T)(el: => T): T = mock[T]

  "Branching Syntax" should "compile" in:
    "language.branch(false)(1)(2)" should compile
    "language.branch(1)(1)(2)" shouldNot typeCheck
