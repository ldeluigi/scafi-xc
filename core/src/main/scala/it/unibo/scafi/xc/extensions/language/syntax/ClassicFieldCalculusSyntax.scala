package it.unibo.scafi.xc.extensions.language.syntax

import it.unibo.scafi.xc.extensions.language.AggregateFoundation

trait ClassicFieldCalculusSyntax[L <: AggregateFoundation] {

  extension (language: L) {
    def nbr[V](expr: => language.AggregateValue[V]): language.AggregateValue[V]
    def rep[A](init: => A)(f: A => A): A
    def share[A](init: => A)(f: A => A): A
  }
}
