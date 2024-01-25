package it.unibo.scafi.xc.language.extensions.formal.syntax

import it.unibo.scafi.xc.language.extensions.AggregateFoundation

trait ClassicFieldCalculusSyntax[L <: AggregateFoundation] {

  extension (language: L) {
    def nbr[V](expr: => language.AggregateValue[V]): language.AggregateValue[V]
    def rep[A](init: => A)(f: A => A): A
    def share[A](init: => A)(f: A => A): A
  }
}
