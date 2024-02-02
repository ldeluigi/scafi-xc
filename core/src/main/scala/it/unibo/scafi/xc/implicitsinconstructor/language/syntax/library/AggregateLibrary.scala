package it.unibo.scafi.xc.implicitsinconstructor.language.syntax.library

import it.unibo.scafi.xc.implicitsinconstructor.language.AggregateFoundation

trait AggregateLibrary[AV[_], L <: AggregateFoundation[AV]](using lang: L):
  export lang.given
