package it.unibo.scafi.xc.language.syntax.formal

import it.unibo.scafi.xc.language.AggregateLanguage

trait ExchangeCalculusSyntax[L <: AggregateLanguage] {

  extension (language: L) {

    def exchange[T](initial: language.AggregateValue[T])(
        f: language.AggregateValue[T] => (language.AggregateValue[T], language.AggregateValue[T]) |
          language.AggregateValue[T],
    ): language.AggregateValue[T]
  }
}
