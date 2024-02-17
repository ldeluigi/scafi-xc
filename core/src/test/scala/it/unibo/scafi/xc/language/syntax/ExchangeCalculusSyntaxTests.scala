package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.language.foundation.{ AggregateFoundation, AggregateFoundationMock }
import it.unibo.scafi.xc.language.syntax.common.RetSend

class ExchangeCalculusSyntaxTests extends UnitTest:

  val language: ExchangeCalculusSyntax & AggregateFoundation = new AggregateFoundationMock with ExchangeCalculusSyntax:

    override def exchange[T](initial: AggregateValue[T])(
        f: AggregateValue[T] => RetSend[AggregateValue[T]],
    ): AggregateValue[T] = mock[AggregateValue[T]]

  "ExchangeCalculus Syntax" should "compile" in:
    val field: language.AggregateValue[Boolean] = mock[language.AggregateValue[Boolean]]
    "val _: language.AggregateValue[Boolean] = language.exchange(field)(x => x)" should compile
    "val _: language.AggregateValue[Int] = language.exchange(field)(x => x)" shouldNot typeCheck
