package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.abstractions.Liftable._
import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.extensions.FoldableExtensions._
import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.libraries.CommonLibrary.mux
import it.unibo.scafi.xc.language.libraries.FieldCalculusLibrary.share
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

import Numeric.Implicits._

object GradientLibrary:

  def distanceEstimate[N: Numeric: UpperBounded](using language: AggregateFoundation)(
      neighboursEstimates: language.NeighbouringValue[N],
      distances: language.NeighbouringValue[N],
  ): N = lift(neighboursEstimates, distances)(_ + _).min

  def distanceTo[N: Numeric: UpperBounded](using
      language: AggregateFoundation & FieldCalculusSyntax,
  )(source: Boolean, distances: language.NeighbouringValue[N]): N =
    share[N](summon[UpperBounded[N]].upperBound)(av =>
      mux(source)(summon[Numeric[N]].zero)(distanceEstimate(av, distances)),
    )
