package it.unibo.scafi.xc.language.syntax.formal

import it.unibo.scafi.xc.language.AggregateLanguage

trait ClassicFieldCalculusSyntax[L <: AggregateLanguage] {
  extension (language: L) {
    def nbr[V](expr: => language.AggregateValue[V]): language.AggregateValue[V]
    def rep[A](init: => A)(f: A => A): A
    def share[A](init: => A)(f: A => A): A
  }
}
