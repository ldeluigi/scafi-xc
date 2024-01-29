package it.unibo.scafi.xc.implicits.language.syntax.library

import it.unibo.scafi.xc.implicits.language.AggregateFoundation

trait AggregateLibrary[AV[_], L <: AggregateFoundation[AV]](using lang: L) {
  export lang.given
}
