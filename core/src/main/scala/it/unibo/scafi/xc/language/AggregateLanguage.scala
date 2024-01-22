package it.unibo.scafi.xc.language

import it.unibo.scafi.xc.abstractions.Liftable

trait AggregateLanguage {
  type AggregateValue[T]

  given lift: Liftable[AggregateValue]
}
