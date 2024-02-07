package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

object FieldCalculusLibrary:

  def nbr[T](using language: AggregateFoundation & FieldCalculusSyntax)(expr: => T): language.AggregateValue[T] =
    language.nbr(expr)

  def rep[T](using language: AggregateFoundation & FieldCalculusSyntax)(init: => T)(f: T => T): T =
    language.rep(init)(f)

  def share[T](using language: AggregateFoundation & FieldCalculusSyntax)(init: => T)(
      f: language.AggregateValue[T] => T,
  ): T = language.share(init)(f)
