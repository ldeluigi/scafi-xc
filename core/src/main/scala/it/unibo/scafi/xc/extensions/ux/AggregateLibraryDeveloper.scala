package it.unibo.scafi.xc.extensions.ux

import scala.util.Random

import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.extensions.language.AggregateFoundation
import it.unibo.scafi.xc.extensions.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.extensions.language.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }

object AggregateLibraryDeveloper {
  // libraries can either be syntactic or semantic
  // semantic library make sense only for a specific semantics
  // syntactic library just rely on common syntax between semantics

  // if any library is needed, the import must be explicit:
  import it.unibo.scafi.xc.extensions.language.syntax.library.BasicGradientLibrary.*

  // example of syntactic library that works for many foundations:
  extension [L <: AggregateFoundation: ClassicFieldCalculusSyntax: BranchingSyntax](lang: L) {

    def distanceToGateways[D: Numeric: UpperBounded](
        local: Boolean,
        gateway: Boolean,
        distances: lang.AggregateValue[D],
    ): lang.AggregateValue[D] = {
      lang.branch(local)(summon[UpperBounded[D]].upperBound)(lang.distanceTo[D](gateway, distances))
    }
  }

  // example of semantic library which only works for one foundation
  // in this case we need differentiated messages of xc calculus
  extension (language: ExchangeCalculusSemantics) {

    def randomMessages(): language.AggregateValue[Int] =
      language.neighbors.map(_ => Random.nextInt())
  }
}
