package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.language.foundation.{ AggregateFoundation, AggregateFoundationMock }
import it.unibo.scafi.xc.language.syntax.common.RetSend

class FieldCalculusSyntaxTests extends UnitTest:

  val language: FieldCalculusSyntax & AggregateFoundation = new AggregateFoundationMock with FieldCalculusSyntax:

    override def nbr[A](expr: A): AggregateValue[A] = mock[AggregateValue[A]]
    override def rep[A](init: A)(f: A => A): A = mock[A]
    override def share[A](init: A)(f: AggregateValue[A] => A): A = mock[A]

  "FieldCalculus Syntax" should "compile" in:
    "val _: language.AggregateValue[Boolean] = language.nbr(true)" should compile
    "val _: language.AggregateValue[Int] = language.nbr(true)" shouldNot typeCheck

    "val _: Boolean = language.rep(true)(x => x)" should compile
    "val _: String = language.rep(true)(x => x)" shouldNot typeCheck

    "val _: Boolean = language.share(true)(x => x.onlySelf)" should compile
    "val _: String = language.share(true)(x => x.onlySelf)" shouldNot typeCheck
