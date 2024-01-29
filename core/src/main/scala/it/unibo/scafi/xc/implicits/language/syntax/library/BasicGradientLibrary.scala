package it.unibo.scafi.xc.implicits.language.syntax.library

import scala.math.Numeric.Implicits.*

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.implicits.language.AggregateFoundation
import it.unibo.scafi.xc.implicits.language.syntax.BranchingSyntax
import it.unibo.scafi.xc.implicits.language.syntax.ClassicFieldCalculusSyntax

class BasicGradientLibrary[AV[_], L <: AggregateFoundation[AV]](using
    lang: L,
    syntax: ClassicFieldCalculusSyntax[AV, L],
    branching: BranchingSyntax[AV, L],
) extends AggregateLibrary {

  import syntax._
  import branching._

  def distanceEstimate[D: Numeric: UpperBounded](estimates: AV[D], distances: AV[D]): D =
    Liftable
      .lift(estimates, distances)(_ + _)
      .nfold(summon[UpperBounded[D]].upperBound)(
        summon[Numeric[D]].min,
      )

  def distanceTo[D: Numeric: UpperBounded](source: Boolean, distances: AV[D]): D =
    rep(summon[UpperBounded[D]].upperBound)(prev =>
      branch(source)(summon[Numeric[D]].zero)(distanceEstimate[D](prev, distances)).onlySelf,
    )

  def hopDistance[D: Numeric: UpperBounded](source: Boolean): D =
    distanceTo(source, summon[Numeric[D]].one)
}
