package it.unibo.scafi.xc.language.libraries

import scala.math.Numeric.Implicits.*

import it.unibo.scafi.xc.abstractions.Liftable
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.libraries.CommonLibrary.mux
import it.unibo.scafi.xc.language.libraries.FieldCalculusLibrary.share
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

object GradientLibrary:

  def distanceEstimate[D: Numeric: UpperBounded](using language: AggregateFoundation)(
      neighboursEstimates: language.AggregateValue[D],
      distances: language.AggregateValue[D],
  ): D =
    Liftable
      .lift(neighboursEstimates, distances)(_ + _)
      .fold(summon[UpperBounded[D]].upperBound)(summon[Numeric[D]].min)

  def distanceTo[D: Numeric: UpperBounded](using
      language: AggregateFoundation with FieldCalculusSyntax,
  )(source: Boolean, distances: language.AggregateValue[D]): D =
    share[D](summon[UpperBounded[D]].upperBound)(av =>
      mux(source)(summon[Numeric[D]].zero)(distanceEstimate(av, distances)),
    ).onlySelf
