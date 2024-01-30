package it.unibo.scafi.xc.implicitsinconstructor.language.syntax

import it.unibo.scafi.xc.implicitsinconstructor.language.AggregateFoundation

trait ExpressiveFieldCalculusSyntax[AV[_], L <: AggregateFoundation[AV]](using L) {
  def nbr[V](expr: => AV[V]): AV[V]

  def rep[A](init: => AV[A])(f: AV[A] => AV[A]): AV[A]

  def share[A](init: => AV[A])(f: AV[A] => AV[A]): AV[A]
}
