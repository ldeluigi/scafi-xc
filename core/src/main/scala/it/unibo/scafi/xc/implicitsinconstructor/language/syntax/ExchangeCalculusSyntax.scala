package it.unibo.scafi.xc.implicitsinconstructor.language.syntax

import it.unibo.scafi.xc.implicitsinconstructor.language.AggregateFoundation

trait ExchangeCalculusSyntax[AV[_], L <: AggregateFoundation[AV]](using L) {

  def exchange[T](initial: AV[T])(f: AV[T] => (AV[T], AV[T]) | AV[T]): AV[T]
}