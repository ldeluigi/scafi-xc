package it.unibo.scafi.xc.language.extensions.syntax

import it.unibo.scafi.xc.language.extensions.AggregateFoundation

trait ExchangeCalculusSyntax[L <: AggregateFoundation] {

  extension (language: L) {

    def exchange[T](initial: language.AggregateValue[T])(
        f: language.AggregateValue[T] => (language.AggregateValue[T], language.AggregateValue[T]) |
          language.AggregateValue[T],
    ): language.AggregateValue[T]
  }
}
