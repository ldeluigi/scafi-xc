package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.foundation.AggregateFoundation

trait ExchangeCalculusSyntax:
  self: AggregateFoundation =>

  def exchange[T](initial: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]) | AggregateValue[T],
  ): AggregateValue[T]
