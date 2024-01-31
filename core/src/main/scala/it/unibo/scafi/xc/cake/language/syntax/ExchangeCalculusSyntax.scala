package it.unibo.scafi.xc.cake.language.syntax

import it.unibo.scafi.xc.cake.language.AggregateFoundation

trait ExchangeCalculusSyntax { self: AggregateFoundation =>

  def exchange[T](initial: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]) | AggregateValue[T],
  ): AggregateValue[T]

}
