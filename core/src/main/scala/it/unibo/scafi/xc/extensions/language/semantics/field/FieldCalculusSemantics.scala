package it.unibo.scafi.xc.extensions.language.semantics.field

import it.unibo.scafi.xc.extensions.language.AggregateFoundation
import it.unibo.scafi.xc.extensions.language.syntax.ClassicFieldCalculusSyntax

trait FieldCalculusSemantics extends AggregateFoundation:
  type Field[T]
  override type AggregateValue[T] = Field[T]

  protected def nbr[A](expr: => Field[A]): Field[A]

  protected def rep[A](init: => A)(fun: A => A): A

  protected def share[A](init: => A)(fun: A => A): A

object FieldCalculusSemantics:

  given ClassicFieldCalculusSyntax[FieldCalculusSemantics] with

    extension (language: FieldCalculusSemantics)

      override def nbr[A](expr: => language.Field[A]): language.Field[A] =
        language.nbr(expr)

      override def rep[A](init: => A)(f: A => A): A =
        language.rep(init)(f)

      override def share[A](init: => A)(f: A => A): A =
        language.share(init)(f)
