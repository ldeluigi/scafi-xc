package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.common.RetSend

trait ExchangeCalculusSyntax:
  self: AggregateFoundation =>

  def exchange[T](initial: AggregateValue[T])(f: AggregateValue[T] => RetSend[AggregateValue[T]]): AggregateValue[T]
