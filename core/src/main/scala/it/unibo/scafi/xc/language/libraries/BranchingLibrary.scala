package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.language.syntax.BranchingSyntax

object BranchingLibrary:

  def branch[T](using language: BranchingSyntax)(cond: Boolean)(th: => T)(el: => T): T =
    language.branch(cond)(th)(el)
