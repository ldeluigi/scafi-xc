package it.unibo.scafi.xc.language.syntax.formal

import it.unibo.scafi.xc.language.Language

trait BranchingSyntax[L <: Language] {
  extension (language: L) def branch[T](cond: language.Value[Boolean])(th: => language.Value[T])(el: => language.Value[T]): language.Value[T]
}
