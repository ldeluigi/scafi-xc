package it.unibo.scafi.xc.language.extensions.formal.syntax

import it.unibo.scafi.xc.language.extensions.AggregateFoundation

trait ExpressiveFieldCalculusSyntax[L <: AggregateFoundation] {

  extension (language: L) {

    def nbr[V](expr: => language.AggregateValue[V]): language.AggregateValue[V]

    def rep[A](init: => language.AggregateValue[A])(
        f: language.AggregateValue[A] => language.AggregateValue[A],
    ): language.AggregateValue[A]

    def share[A](init: => language.AggregateValue[A])(
        f: language.AggregateValue[A] => language.AggregateValue[A],
    ): language.AggregateValue[A]
  }
}
