package it.unibo.scafi.xc.language.syntax.formal

import it.unibo.scafi.xc.language.Language

trait FieldCalculusSyntax[L <: Language] {

  extension (language: L) {
    def neighbouring[A](expr: => language.Value[A]): language.Value[A]
    def repeating[A](init: language.Value[A])(f: language.Value[A] => language.Value[A]): language.Value[A]
  }
}
