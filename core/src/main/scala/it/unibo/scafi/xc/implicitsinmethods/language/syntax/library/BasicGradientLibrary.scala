package it.unibo.scafi.xc.implicitsinmethods.language.syntax.library

import scala.math.Numeric.Implicits.*

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.implicitsinmethods.language.AggregateFoundation
import it.unibo.scafi.xc.implicitsinmethods.language.syntax.BranchingSyntax
import it.unibo.scafi.xc.implicitsinmethods.language.syntax.ClassicFieldCalculusSyntax

object BasicGradientLibrary:

  def distanceEstimate[AV[_], L <: AggregateFoundation[AV], D: Numeric: UpperBounded](using l: L)(
      estimates: AV[D],
      distances: AV[D],
  ): D =
    import l.lift
    Liftable
      .lift(estimates, distances)(_ + _)
      .nfold(summon[UpperBounded[D]].upperBound)(
        summon[Numeric[D]].min,
      )

  def distanceTo[AV[_], L <: AggregateFoundation[AV], D: Numeric: UpperBounded](using
      l: L,
      b: BranchingSyntax[AV, L],
      f: ClassicFieldCalculusSyntax[AV, L],
  )(source: Boolean, distances: AV[D]): D =
    import l.{ _, given }, b._, f._
    rep(summon[UpperBounded[D]].upperBound)(prev =>
      branch(source)(summon[Numeric[D]].zero)(distanceEstimate[AV, L, D](prev, distances)).onlySelf,
    )

  def hopDistance[AV[_], L <: AggregateFoundation[AV], D: Numeric: UpperBounded](using
      l: L,
      b: BranchingSyntax[AV, L],
      f: ClassicFieldCalculusSyntax[AV, L],
  )(source: Boolean): D =
    import l.convert
    distanceTo[AV, L, D](source, summon[Numeric[D]].one)
end BasicGradientLibrary
