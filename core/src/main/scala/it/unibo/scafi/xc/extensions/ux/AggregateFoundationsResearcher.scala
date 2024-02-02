package it.unibo.scafi.xc.extensions.ux

import it.unibo.scafi.xc.abstractions.boundaries.UpperBounded
import it.unibo.scafi.xc.extensions.language.AggregateFoundation
import it.unibo.scafi.xc.extensions.language.syntax.library.BasicGradientLibrary.distanceTo
import it.unibo.scafi.xc.extensions.language.syntax.{ BranchingSyntax, ClassicFieldCalculusSyntax }

object AggregateFoundationsResearcher:

  // researchers can experiment with different foundations
  trait NewSemantics extends AggregateFoundation:
    // anything that conforms to AggregateFoundation can be implemented
    def magic[T](aggregateValue: AggregateValue[T]): AggregateValue[T]

    given moreMagic[T]: Conversion[AggregateValue[T], T]

  // then, researcher can "demonstrate" the expressiveness of their foundations
  // by implementing *given* instances of syntaxes
  object NewSemantics:

    given ClassicFieldCalculusSyntax[NewSemantics] with

      extension (language: NewSemantics)

        override def nbr[V](expr: => language.AggregateValue[V]): language.AggregateValue[V] =
          language.magic(expr)

        override def rep[A](init: => A)(f: A => A): A =
          language.magic(init)

        override def share[A](init: => A)(f: A => A): A =
          language.magic(init)

    given BranchingSyntax[NewSemantics] with

      extension (language: NewSemantics)

        override def branch[T](cond: language.AggregateValue[Boolean])(th: => language.AggregateValue[T])(
            el: => language.AggregateValue[T],
        ): language.AggregateValue[T] =
          language.magic(th)

  end NewSemantics

  // after which they can use their semantics with classic libraries
  object LibraryThatCaresAboutSemantics:
    import it.unibo.scafi.xc.extensions.language.syntax.library.BasicGradientLibrary

    extension (language: NewSemantics)

      def magicGradient[D: Numeric: UpperBounded](distances: language.AggregateValue[D]): language.AggregateValue[D] =
        language.distanceTo[D](true, distances)
end AggregateFoundationsResearcher
