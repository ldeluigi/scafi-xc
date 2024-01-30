package it.unibo.scafi.xc.implicits.language.semantics.field

import it.unibo.scafi.xc.implicits.language.AggregateFoundation
import it.unibo.scafi.xc.implicits.language.syntax.ClassicFieldCalculusSyntax

trait FieldCalculusSemantics[Field[_]] extends AggregateFoundation[Field] {
  protected def nbr[A](expr: => Field[A]): Field[A]

  protected def rep[A](init: => A)(fun: A => A): A

  protected def share[A](init: => A)(fun: A => A): A
}

object FieldCalculusSemantics {

  given [Field[_]](using
      semantics: FieldCalculusSemantics[Field],
  ): ClassicFieldCalculusSyntax[Field, FieldCalculusSemantics[Field]] with {
    override def nbr[V](expr: => Field[V]): Field[V] = semantics.nbr(expr)

    override def rep[A](init: => A)(f: A => A): A = semantics.rep(init)(f)

    override def share[A](init: => A)(f: A => A): A = semantics.share(init)(f)
  }
}
