package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.abstractions.Liftable._
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.libraries.CommonLibrary.mux
import it.unibo.scafi.xc.language.libraries.FieldCalculusLibrary.share
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

import Numeric.Implicits._

object GradientLibrary:

  def distanceEstimate[N: Numeric: UpperBounded](using language: AggregateFoundation)(
      neighboursEstimates: language.AggregateValue[N],
      distances: language.AggregateValue[N],
  ): N =
    lift(neighboursEstimates, distances)(_ + _)
      .fold(summon[UpperBounded[N]].upperBound)(summon[Numeric[N]].min)

  def distanceTo[N: Numeric: UpperBounded](using
      language: AggregateFoundation with FieldCalculusSyntax,
  )(source: Boolean, distances: language.AggregateValue[N]): N =
    share[N](summon[UpperBounded[N]].upperBound)(av =>
      mux(source)(summon[Numeric[N]].zero)(distanceEstimate(av, distances)),
    ).onlySelf