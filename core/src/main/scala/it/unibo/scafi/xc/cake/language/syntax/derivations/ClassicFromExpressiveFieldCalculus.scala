package it.unibo.scafi.xc.cake.language.syntax.derivations

import it.unibo.scafi.xc.cake.language.AggregateFoundation
import it.unibo.scafi.xc.cake.language.syntax.ClassicFieldCalculusSyntax

trait ClassicFromExpressiveFieldCalculus extends ClassicFieldCalculusSyntax {
  self: AggregateFoundation =>

  // val delegate: ExpressiveFieldCalculusSyntax = this // PROBLEM: this.AggregateValue is not the same as delegate.AggregateValue anymore! Maybe try generics?

//  override def nbr[V](expr: => AggregateValue[V]): AggregateValue[V] =
//    super[ExpressiveFieldCalculusSyntax].nbr(expr)
//
//  override def rep[A](init: => A)(f: A => A): A =
//    super[ExpressiveFieldCalculusSyntax].rep[A](init)(nv => nv.map(f)).onlySelf
//
//  override def share[A](init: => A)(f: A => A): A =
//    super[ExpressiveFieldCalculusSyntax].share(init)(nv => nv.map(f)).onlySelf

}
