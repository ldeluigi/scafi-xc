package it.unibo.scafi.xc.implicitsinconstructor.runtime

import it.unibo.scafi.xc.implicitsinconstructor.language.AggregateFoundation

trait Core {
  type Context
  type Export
  type Language <: AggregateFoundation[_]
}
