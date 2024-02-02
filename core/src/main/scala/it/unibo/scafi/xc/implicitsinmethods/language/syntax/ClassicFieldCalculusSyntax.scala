package it.unibo.scafi.xc.implicitsinmethods.language.syntax

import it.unibo.scafi.xc.implicitsinmethods.language.AggregateFoundation

trait ClassicFieldCalculusSyntax[AV[_], L <: AggregateFoundation[AV]](using L):
  def nbr[V](expr: => AV[V]): AV[V]

  def rep[A](init: => A)(f: A => A): A

  def share[A](init: => A)(f: A => A): A
