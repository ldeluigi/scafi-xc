package it.unibo.scafi.xc.implicitsinmethods.ux

import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.implicitsinmethods.language.AggregateFoundation
import it.unibo.scafi.xc.implicitsinmethods.language.syntax.library.BasicGradientLibrary._
import it.unibo.scafi.xc.implicitsinmethods.language.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }

object AggregateFoundationsResearcher {
  // a new way to view aggregate values
  trait NewAggregateValueConcept[T]

  // researchers can experiment with different foundations
  trait NewSemantics extends AggregateFoundation[NewAggregateValueConcept] {
    // anything that conforms to AggregateFoundation can be implemented
    def magic[T](aggregateValue: AggregateValue[T]): AggregateValue[T]

    given moreMagic[T]: Conversion[AggregateValue[T], T]
  }

  // then, researcher can "demonstrate" the expressiveness of their foundations
  // by implementing *given* instances of syntaxes
  object NewSemantics {

    class NewSyntaxImpl(using s: NewSemantics)
        extends ClassicFieldCalculusSyntax[NewAggregateValueConcept, NewSemantics] {

      import s.{ *, given }

      override def nbr[V](expr: => NewAggregateValueConcept[V]): NewAggregateValueConcept[V] = magic(expr)

      override def rep[A](init: => A)(f: A => A): A = magic(init)

      override def share[A](init: => A)(f: A => A): A = magic(init)
    }

    class NewSyntaxImpl2(using s: NewSemantics) extends BranchingSyntax[NewAggregateValueConcept, NewSemantics] {

      import s.*

      override def branch[T](cond: NewAggregateValueConcept[Boolean])(th: => NewAggregateValueConcept[T])(
          el: => NewAggregateValueConcept[T],
      ): NewAggregateValueConcept[T] =
        magic(th)
    }
  }

  // after which they can use their semantics with classic libraries to make semantic libraries
  object LibraryThatCaresAboutSemantics {

    def magicGradient[D: Numeric: UpperBounded](using
        newSemantics: NewSemantics,
        classicSyntax: ClassicFieldCalculusSyntax[newSemantics.AggregateValue, NewSemantics],
        branchingSyntax: BranchingSyntax[newSemantics.AggregateValue, NewSemantics],
    )(
        distances: NewAggregateValueConcept[D],
    ): NewAggregateValueConcept[D] = {
      import newSemantics.{ _, given }
      distanceTo[newSemantics.AggregateValue, NewSemantics, D](true, distances)
    }
  }
}
