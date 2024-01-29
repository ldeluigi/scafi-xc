package it.unibo.scafi.xc.extensions.language.syntax.library

import scala.math.Numeric.Implicits.*

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.extensions.language.AggregateFoundation
import it.unibo.scafi.xc.extensions.language.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }

object BasicGradientLibrary {

  extension [L <: AggregateFoundation: ClassicFieldCalculusSyntax: BranchingSyntax](language: L) {

    def distanceEstimate[D: Numeric: UpperBounded](
        estimates: language.AggregateValue[D],
        distances: language.AggregateValue[D],
    ): D = Liftable
      .lift(estimates, distances)(_ + _)
      .nfold(summon[UpperBounded[D]].upperBound)(
        summon[Numeric[D]].min,
      )

    def distanceTo[D: Numeric: UpperBounded](
        source: Boolean,
        distances: language.AggregateValue[D],
    ): D =
      language.rep[D](summon[UpperBounded[D]].upperBound)(n =>
        language.branch(source)(summon[Numeric[D]].zero)(distanceEstimate[D](n, distances)).onlySelf,
      )

    def hopDistance[D: Numeric: UpperBounded](source: Boolean): D =
      distanceTo(source, summon[Numeric[D]].one)
  }
}
