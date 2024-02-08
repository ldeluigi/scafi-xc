package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.ExchangeCalculusSyntax
import it.unibo.scafi.xc.language.syntax.common.RetSend

object ExchangeCalculusLibrary:

  def exchange[T](using language: AggregateFoundation & ExchangeCalculusSyntax)(initial: language.AggregateValue[T])(
      f: language.AggregateValue[T] => RetSend[language.AggregateValue[T]],
  ): language.AggregateValue[T] = language.exchange(initial)(f)
