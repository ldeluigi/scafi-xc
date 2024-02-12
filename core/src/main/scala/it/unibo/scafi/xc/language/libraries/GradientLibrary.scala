package it.unibo.scafi.xc.language.libraries

import scala.runtime.stdLibPatches.Predef.summon

import it.unibo.scafi.xc.abstractions.Liftable.*
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.libraries.CommonLibrary.mux
import it.unibo.scafi.xc.language.sensors.DistanceSensor.senseDistance
import it.unibo.scafi.xc.language.libraries.FieldCalculusLibrary.share
import it.unibo.scafi.xc.language.sensors.DistanceSensor
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

import Numeric.Implicits.*

object GradientLibrary:

  def distanceEstimate[N: Numeric: UpperBounded](using language: AggregateFoundation)(
      neighboursEstimates: language.AggregateValue[N],
      distances: language.AggregateValue[N],
  ): N = lift(neighboursEstimates, distances)(_ + _).withoutSelf.min

  def distanceTo[N: Numeric: UpperBounded](using
      language: AggregateFoundation & FieldCalculusSyntax,
  )(source: Boolean, distances: language.AggregateValue[N]): N =
    share[N](summon[UpperBounded[N]].upperBound)(av =>
      mux(source)(summon[Numeric[N]].zero)(distanceEstimate(av, distances)),
    )

  def sensorDistanceEstimate[N: Numeric: UpperBounded](using
      language: AggregateFoundation & DistanceSensor[N],
  )(neighboursEstimates: language.AggregateValue[N]): N =
    distanceEstimate(neighboursEstimates, senseDistance[N])

  def sensorDistanceTo[N: Numeric: UpperBounded](using
      language: AggregateFoundation & FieldCalculusSyntax & DistanceSensor[N],
  )(source: Boolean): N =
    distanceTo(source, senseDistance[N])
end GradientLibrary
