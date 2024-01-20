package it.unibo.scafi.xc.language.syntax.formal

import scala.annotation.targetName

import it.unibo.scafi.xc.language.Language

trait ExchangeCalculusSyntax[L <: Language] {
  extension (language: L) {
    def exchange[T](initial: language.Value[T], f: language.Value[T] => (language.Value[T], language.Value[T])): language.Value[T]

    @targetName("exchange_1")
    def exchange[T](initial: language.Value[T], f: language.Value[T] => language.Value[T]): language.Value[T] =
      language.exchange(initial, f.andThen(v => (v, v)))
  }
}
