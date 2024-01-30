package it.unibo.scafi.xc.implicits.runtime

import it.unibo.scafi.xc.implicits.language.AggregateFoundation

trait Core {
  type Context
  type Export
  type Language <: AggregateFoundation[_]
}
