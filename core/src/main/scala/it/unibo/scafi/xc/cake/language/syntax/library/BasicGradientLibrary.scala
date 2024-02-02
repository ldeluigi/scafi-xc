package it.unibo.scafi.xc.cake.language.syntax.library

import scala.math.Numeric.Implicits.*

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.cake.language.AggregateFoundation
import it.unibo.scafi.xc.cake.language.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }

trait BasicGradientLibrary:
  self: AggregateFoundation with ClassicFieldCalculusSyntax with BranchingSyntax =>

  def distanceEstimate[D: Numeric: UpperBounded](
      estimates: AggregateValue[D],
      distances: AggregateValue[D],
  ): D = Liftable
    .lift(estimates, distances)(_ + _)
    .nfold(summon[UpperBounded[D]].upperBound)(
      summon[Numeric[D]].min,
    )

  def distanceTo[D: Numeric: UpperBounded](
      source: Boolean,
      distances: AggregateValue[D],
  ): D =
    rep[D](summon[UpperBounded[D]].upperBound)(n =>
      branch(source)(summon[Numeric[D]].zero)(distanceEstimate[D](n, distances)).onlySelf,
    )

  def hopDistance[D: Numeric: UpperBounded](source: Boolean): D =
    distanceTo(source, summon[Numeric[D]].one)
end BasicGradientLibrary
