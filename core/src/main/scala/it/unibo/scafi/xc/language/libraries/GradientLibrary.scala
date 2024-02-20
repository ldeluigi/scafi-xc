package it.unibo.scafi.xc.language.libraries

import scala.runtime.stdLibPatches.Predef.summon

import it.unibo.scafi.xc.abstractions.Liftable.*
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.sensors.DistanceSensor.senseDistance
import it.unibo.scafi.xc.language.sensors.DistanceSensor
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

import FieldCalculusLibrary.share
import CommonLibrary.mux
import Numeric.Implicits.*

/**
 * This library provides a set of functions to compute the distance between nodes and a source in a network.
 */
object GradientLibrary:

  /**
   * This function computes the distance estimate from a source to this node, based on estimates from the node's
   * neighbours and the distances from the neighbours.
   * @param neighboursEstimates
   *   the estimates from the neighbours
   * @param distances
   *   the distances from the neighbours
   * @tparam N
   *   the type of the distance
   * @return
   *   the distance estimate from a source to a node
   */
  def distanceEstimate[N: Numeric: UpperBounded](using language: AggregateFoundation)(
      neighboursEstimates: language.AggregateValue[N],
      distances: language.AggregateValue[N],
  ): N = lift(neighboursEstimates, distances)(_ + _).withoutSelf.min

  /**
   * This function computes the distance from a source to this node, by sharing the distance estimate with the
   * neighbours and computing the minimum distance estimate.
   * @param source
   *   whether this node is the source
   * @param distances
   *   the measured distances from the neighbours
   * @tparam N
   *   the type of the distance
   * @return
   *   the distance from the source to this node
   */
  def distanceTo[N: Numeric: UpperBounded](using
      language: AggregateFoundation & FieldCalculusSyntax,
  )(source: Boolean, distances: language.AggregateValue[N]): N =
    share[N](summon[UpperBounded[N]].upperBound)(av =>
      mux(source)(summon[Numeric[N]].zero)(distanceEstimate(av, distances)),
    )

  /**
   * This function computes the distance estimate from a source to this node, based on estimates from the node's
   * neighbours and the distances from the neighbours measured by a distance sensor.
   * @param neighboursEstimates
   *   the estimates from the neighbours
   * @tparam N
   *   the type of the distance
   * @return
   *   the distance estimate from a source to a node
   * @see
   *   [[DistanceSensor.senseDistance]]
   */
  def sensorDistanceEstimate[N: Numeric: UpperBounded](using
      language: AggregateFoundation & DistanceSensor[N],
  )(neighboursEstimates: language.AggregateValue[N]): N =
    distanceEstimate(neighboursEstimates, senseDistance[N])

  /**
   * This function computes the distance from a source to this node, by sharing the distance estimate with the
   * neighbours and computing the minimum distance estimate. The distances are measured by a distance sensor.
   * @param source
   *   whether this node is the source
   * @tparam N
   *   the type of the distance
   * @return
   *   the distance from the source to this node
   * @see
   *   [[DistanceSensor.senseDistance]]
   */
  def sensorDistanceTo[N: Numeric: UpperBounded](using
      language: AggregateFoundation & FieldCalculusSyntax & DistanceSensor[N],
  )(source: Boolean): N =
    distanceTo(source, senseDistance[N])
end GradientLibrary
