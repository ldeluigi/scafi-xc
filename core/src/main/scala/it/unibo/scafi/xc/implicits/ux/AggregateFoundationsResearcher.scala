package it.unibo.scafi.xc.implicits.ux

import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.implicits.language.AggregateFoundation
import it.unibo.scafi.xc.implicits.language.syntax.library.BasicGradientLibrary
import it.unibo.scafi.xc.implicits.language.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }
import it.unibo.scafi.xc.implicits.ux.AggregateFoundationsResearcher.NewSemantics.NewSyntaxImpl2

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
  class LibraryThatCaresAboutSemantics(using
      lang: NewSemantics,
      gradient: BasicGradientLibrary[NewAggregateValueConcept, NewSemantics],
  ) {

    import gradient._
    import lang.given

    def magicGradient[D: Numeric: UpperBounded](using newSemantics: NewSemantics)(
        distances: NewAggregateValueConcept[D],
    ): NewAggregateValueConcept[D] =
      distanceTo[D](true, distances)
  }

  // alternatively, libraries can instantiate their dependencies
  class LibraryThatCaresAboutSemantics2(using
      lang: NewSemantics,
  ) {
    given NewSemantics.NewSyntaxImpl = NewSemantics.NewSyntaxImpl()
    given NewSyntaxImpl2 = NewSyntaxImpl2()
    private val gradient = BasicGradientLibrary[NewAggregateValueConcept, NewSemantics]()

    import gradient._
    import lang.given

    def magicGradient[D: Numeric: UpperBounded](using newSemantics: NewSemantics)(
        distances: NewAggregateValueConcept[D],
    ): NewAggregateValueConcept[D] =
      distanceTo[D](true, distances)
  }
}
