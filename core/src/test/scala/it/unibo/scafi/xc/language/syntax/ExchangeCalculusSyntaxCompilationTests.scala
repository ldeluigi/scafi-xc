package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.language.foundation.DistributedSystemUtilities.Shareable
import it.unibo.scafi.xc.language.foundation.{ AggregateFoundation, AggregateFoundationMock }
import it.unibo.scafi.xc.language.syntax.common.RetSend
import it.unibo.scafi.xc.language.syntax.common.RetSend.*

class ExchangeCalculusSyntaxCompilationTests extends UnitTest:

  val language: ExchangeCalculusSyntax & AggregateFoundation = new AggregateFoundationMock with ExchangeCalculusSyntax:

    override def exchange[T: Shareable](initial: AggregateValue[T])(
        f: AggregateValue[T] => RetSend[AggregateValue[T]],
    ): AggregateValue[T] = mock[AggregateValue[T]]

  val field: language.AggregateValue[Boolean] = mock[language.AggregateValue[Boolean]]

  "ExchangeCalculus Syntax" should "compile" in:
    val intField = mock[language.AggregateValue[Int]]
    "val _: language.AggregateValue[Boolean] = language.exchange(field)(x => x)" should compile
    "val _: language.AggregateValue[Int] = language.exchange(intField)(x => ret (x) send x)" should compile
    "val _: language.AggregateValue[Boolean] = language.exchange(field)(x => (x, x))" should compile
    "val _: language.AggregateValue[Int] = language.exchange(field)(x => x)" shouldNot typeCheck

  it should "not compile if exchanging non serializable values or aggregate values" in:
    "val _ = language.exchange(field.map(x => 1))(x => x)" should compile
    "val _ = language.exchange(field.map(x => field))(x => x)" shouldNot compile
    "val _ = language.exchange(new Object)(x => x)" shouldNot compile
end ExchangeCalculusSyntaxCompilationTests
