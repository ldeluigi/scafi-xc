package it.unibo.scafi.xc.extensions.runtime

import it.unibo.scafi.xc.extensions.language.AggregateFoundation

trait Core:
  type Context
  type Export
  type Language <: AggregateFoundation
