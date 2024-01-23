package it.unibo.scafi.xc.language.syntax.formal

import it.unibo.scafi.xc.language.AggregateLanguage

trait ExpressiveFieldCalculusSyntax[L <: AggregateLanguage] {

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
