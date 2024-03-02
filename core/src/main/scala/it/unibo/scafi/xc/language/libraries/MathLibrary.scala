package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax
import it.unibo.scafi.xc.language.foundation.DistributedSystemUtilities.Shareable

import FieldCalculusLibrary.nbr
import FoldingLibrary.nfold
import Fractional.Implicits.*

/**
 * This library provides a set of mathematical functions.
 */
object MathLibrary:

  /**
   * Given a weight and a value, computes the weighted average of a value across the immediate neighbours of the current
   * device.
   * @param weight
   *   the weight of the value for this device
   * @param value
   *   the value to be averaged
   * @tparam N
   *   the type of the value
   * @return
   *   the weighted average of the value
   */
  def average[N: Fractional: Shareable](using
      language: AggregateFoundation & FieldCalculusSyntax,
  )(weight: N, value: N): N =
    val totW = nbr(weight).nfold(weight)(_ + _)
    val totV = nbr(weight * value).nfold(weight * value)(_ + _)
    totV / totW
end MathLibrary
