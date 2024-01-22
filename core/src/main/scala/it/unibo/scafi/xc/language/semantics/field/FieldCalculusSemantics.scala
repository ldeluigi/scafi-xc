package it.unibo.scafi.xc.language.semantics.field

import it.unibo.scafi.xc.language.AggregateLanguage
import it.unibo.scafi.xc.language.syntax.formal.ClassicFieldCalculusSyntax

trait FieldCalculusSemantics extends AggregateLanguage {
  type Field[T]
  type ID
  type CNAME
  override type AggregateValue[T] = Field[T]

  def nbr[A](expr: => Field[A]): Field[A]
  def rep[A](init: => A)(fun: A => A): A
  def share[A](init: => A)(fun: A => A): A
  def foldhood[A](init: => A)(fun: (A, A) => A)(expr: => Field[A]): Field[A]
  protected def mid(): Field[ID]
}

object FieldCalculusSemantics {
  given ClassicFieldCalculusSyntax[FieldCalculusSemantics] with {
    extension (language: FieldCalculusSemantics) override def nbr[A](expr: => language.Field[A]): language.Field[A] =
        language.nbr(expr)
    extension (language: FieldCalculusSemantics) override def rep[A](init: => A)(f: A => A): A =
        language.rep(init)(f)
    extension (language: FieldCalculusSemantics) override def share[A](init: => A)(f: A => A): A =
        language.share(init)(f)
  }
}
