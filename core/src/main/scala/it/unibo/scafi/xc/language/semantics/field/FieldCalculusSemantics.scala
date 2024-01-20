package it.unibo.scafi.xc.language.semantics.field

import it.unibo.scafi.xc.language.Language
import it.unibo.scafi.xc.language.syntax.formal.FieldCalculusSyntax

trait FieldCalculusSemantics extends Language {
  type Field[T]
  type ID
  type CNAME
  override type Value[T] = Field[T]

  def nbr[A](expr: => Field[A]): Field[A]
  def rep[A](init: => A)(fun: A => A): A
  def mid(): Field[ID]
}

object FieldCalculusSemantics {
  given FieldCalculusSyntax[FieldCalculusSemantics] with {
    extension (language: FieldCalculusSemantics) {
      override def neighbouring[T](expr: => language.Field[T]): language.Field[T] = {
        language.nbr(expr)
      }
      override def repeating[A](init: language.Field[A])(f: language.Field[A] => language.Field[A]): language.Field[A] =
        language.rep(init)(f)
    }
  }
}
