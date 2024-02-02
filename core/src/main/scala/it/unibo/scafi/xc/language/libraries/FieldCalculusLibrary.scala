package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

object FieldCalculusLibrary:

  def nbr[T](using language: AggregateFoundation with FieldCalculusSyntax)(
      expr: => language.AggregateValue[T],
  ): language.AggregateValue[T] = language.nbr(expr)

  def rep[T](using language: AggregateFoundation with FieldCalculusSyntax)(init: => language.AggregateValue[T])(
      f: language.AggregateValue[T] => language.AggregateValue[T],
  ): language.AggregateValue[T] = language.rep(init)(f)

  def share[T](using language: AggregateFoundation with FieldCalculusSyntax)(init: => language.AggregateValue[T])(
      f: language.AggregateValue[T] => language.AggregateValue[T],
  ): language.AggregateValue[T] = language.share(init)(f)
