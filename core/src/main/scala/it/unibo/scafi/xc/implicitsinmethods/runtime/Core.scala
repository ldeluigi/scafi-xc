package it.unibo.scafi.xc.implicitsinmethods.runtime

import it.unibo.scafi.xc.implicitsinmethods.language.AggregateFoundation

trait Core:
  type Context
  type Export
  type Language <: AggregateFoundation[_]
