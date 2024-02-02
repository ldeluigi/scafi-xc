package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.libraries.FieldCalculusLibrary.*
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

import Fractional.Implicits._

object MathLibrary:

  def average[N: Fractional](using language: AggregateFoundation with FieldCalculusSyntax)(weight: N, value: N): N =
    val totW = nbr(weight).fold(weight)(_ + _)
    val totV = nbr(weight * value).fold(weight * value)(_ + _)
    totV / totW
