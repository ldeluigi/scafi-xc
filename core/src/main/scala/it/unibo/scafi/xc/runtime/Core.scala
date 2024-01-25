package it.unibo.scafi.xc.runtime

import it.unibo.scafi.xc.language.extensions.AggregateFoundation

trait Core {
  type Context
  type Export
  type Language <: AggregateFoundation
}
