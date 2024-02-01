package it.unibo.scafi.xc.language.libraries

import scala.math.Numeric.Implicits._

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.language.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

trait GradientLibrary { self: AggregateFoundation with FieldCalculusSyntax =>

  def distanceEstimate[D: Numeric: UpperBounded](
      neighboursEstimates: AggregateValue[D],
      distances: AggregateValue[D],
  ): D =
    Liftable
      .lift(neighboursEstimates, distances)(_ + _)
      .fold(summon[UpperBounded[D]].upperBound)(summon[Numeric[D]].min)

  def distanceTo[D: Numeric: UpperBounded](source: Boolean, distances: AggregateValue[D]): D =
    share[D](summon[UpperBounded[D]].upperBound)(av =>
      mux(source)(summon[Numeric[D]].zero)(distanceEstimate(av, distances)),
    ).onlySelf
}
