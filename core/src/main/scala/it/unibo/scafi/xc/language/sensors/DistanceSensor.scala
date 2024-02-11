package it.unibo.scafi.xc.language.sensors

import it.unibo.scafi.xc.language.foundation.AggregateFoundation

trait DistanceSensor[N: Numeric]:
  this: AggregateFoundation =>

  def senseDistance: AggregateValue[N]

object DistanceSensor:

  def senseDistance[N: Numeric](using language: AggregateFoundation & DistanceSensor[N]): language.AggregateValue[N] =
    language.senseDistance
