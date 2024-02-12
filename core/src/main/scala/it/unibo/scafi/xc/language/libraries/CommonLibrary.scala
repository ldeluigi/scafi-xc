package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.implementations.boundaries.CommonBoundaries
import it.unibo.scafi.xc.language.foundation.{ AggregateFoundation, DeviceAwareAggregateFoundation }

object CommonLibrary:
  export CommonBoundaries.{ _, given }

  def mux[T](cond: Boolean)(th: T)(el: T): T = if cond then th else el

  def self(using language: DeviceAwareAggregateFoundation): language.DeviceId = language.self

  def device(using
      language: AggregateFoundation & DeviceAwareAggregateFoundation,
  ): language.AggregateValue[language.DeviceId] = language.device
