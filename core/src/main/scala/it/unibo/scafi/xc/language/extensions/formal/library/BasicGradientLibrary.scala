package it.unibo.scafi.xc.language.extensions.formal.library

import scala.math.Numeric.Implicits.*

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.language.extensions.AggregateFoundation
import it.unibo.scafi.xc.language.extensions.formal.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }

trait BasicGradientLibrary {

  extension [L <: AggregateFoundation: ClassicFieldCalculusSyntax: BranchingSyntax](language: L) {

    def distanceEstimate[D: Numeric: UpperBounded](
        estimates: language.AggregateValue[D],
        distances: language.AggregateValue[D],
    ): D = language.nfold(Liftable.lift(estimates, distances)(_ + _))(summon[UpperBounded[D]].upperBound)(
      summon[Numeric[D]].min,
    )

    def distanceTo[D: Numeric: UpperBounded](
        source: Boolean,
        distances: language.AggregateValue[D],
    ): language.AggregateValue[D] =
      language.rep[D](summon[UpperBounded[D]].upperBound)(n =>
        language.onlySelf(language.branch(source)(summon[Numeric[D]].zero)(distanceEstimate[D](n, distances))),
      )

    def hopDistance(source: Boolean): language.AggregateValue[Double] = {
      import it.unibo.scafi.xc.implementations.boundaries.CommonBoundaries.given_Bounded_Double
      distanceTo[Double](source, 1.0)
    }
  }
}
