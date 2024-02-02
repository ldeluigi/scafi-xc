package it.unibo.scafi.xc.implicitsinconstructor.ux

import scala.util.Random

import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.implicitsinconstructor.language.AggregateFoundation
import it.unibo.scafi.xc.implicitsinconstructor.language.semantics.exchange.{ ExchangeCalculusSemantics, NValues }
import it.unibo.scafi.xc.implicitsinconstructor.language.syntax.library.BasicGradientLibrary
import it.unibo.scafi.xc.implicitsinconstructor.language.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }

object AggregateLibraryDeveloper:
  // libraries can either be syntactic or semantic
  // semantic library make sense only for a specific semantics
  // syntactic library just rely on common syntax between semantics

  // if any library is needed, the import must be explicit, and it must be instantiated

  // example of syntactic library that works for many foundations:
  class MyLibrary1[AV[_], L <: AggregateFoundation[AV]](using
      lang: L,
      dependency: BasicGradientLibrary[AV, L],
      branching: BranchingSyntax[AV, L],
  ):

    import branching._
    import dependency._
    import lang.{ _, given }

    def distanceToGateways[D: Numeric: UpperBounded](
        local: Boolean,
        gateway: Boolean,
        distances: lang.AggregateValue[D],
    ): lang.AggregateValue[D] =
      branch(local)(summon[UpperBounded[D]].upperBound)(distanceTo[D](gateway, distances))

  // alternative example of syntactic library that instantiate the dependencies itself:
  class MyLibrary1b[AV[_], L <: AggregateFoundation[AV]](using
      lang: L,
      calculus: ClassicFieldCalculusSyntax[AV, L],
      branching: BranchingSyntax[AV, L],
  ):
    private val dependency = BasicGradientLibrary[AV, L]()

    import dependency._
    import lang.{ _, given }
    import branching._

    def distanceToGateways[D: Numeric: UpperBounded](
        local: Boolean,
        gateway: Boolean,
        distances: lang.AggregateValue[D],
    ): lang.AggregateValue[D] =
      branch(local)(summon[UpperBounded[D]].upperBound)(distanceTo[D](gateway, distances))

  // example of semantic library that works only for a specific foundation:
  class MyLibrary2[ID](using lang: ExchangeCalculusSemantics[ID]):
    import lang._

    def randomMessages(): NValues[ID, Int] =
      neighbors.map(_ => Random.nextInt())
end AggregateLibraryDeveloper
