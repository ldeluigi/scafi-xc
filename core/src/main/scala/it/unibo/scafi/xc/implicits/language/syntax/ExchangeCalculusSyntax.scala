package it.unibo.scafi.xc.implicits.language.syntax

import it.unibo.scafi.xc.implicits.language.AggregateFoundation

trait ExchangeCalculusSyntax[L <: AggregateFoundation] {

  def exchange[T](using language: L)(initial: language.AggregateValue[T])(
      f: language.AggregateValue[T] => (language.AggregateValue[T], language.AggregateValue[T]) |
        language.AggregateValue[T],
  ): language.AggregateValue[T]
}
