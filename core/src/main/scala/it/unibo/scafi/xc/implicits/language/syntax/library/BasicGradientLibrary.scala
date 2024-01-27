package it.unibo.scafi.xc.implicits.language.syntax.library

import scala.math.Numeric.Implicits.*

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.implicits.language.AggregateFoundation
import it.unibo.scafi.xc.implicits.language.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }

object BasicGradientLibrary {

  def distanceEstimate[
      L <: AggregateFoundation: ClassicFieldCalculusSyntax: BranchingSyntax,
      D: Numeric: UpperBounded,
  ](using language: L)(estimates: language.AggregateValue[D], distances: language.AggregateValue[D]): D =
    Liftable
      .lift(estimates, distances)(_ + _)
      .nfold(summon[UpperBounded[D]].upperBound)(
        summon[Numeric[D]].min,
      )
}
