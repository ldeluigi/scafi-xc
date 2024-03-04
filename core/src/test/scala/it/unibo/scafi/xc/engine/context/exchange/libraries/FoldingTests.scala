package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.context.ProbingContextMixin
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

trait FoldingTests:
  this: UnitTest & ProbingContextMixin & BasicFactoryMixin =>

  def foldingSemantics(): Unit =
    var foldingResult: Long = 0
    var neighbouringFoldingResult: Long = 0
    def foldingProgram(using BasicExchangeCalculusContext[Int]): Unit =
      branch(self < 10) {
        foldingResult = nbr(self).fold(1)(_ * _)
        neighbouringFoldingResult = nbr(self).nfold(1)(_ * _)
      } {}

    var exportProbe: Export[Int, ValueTree[InvocationCoordinate, Any]] = probe(
      localId = 2,
      factory = factory,
      program = foldingProgram,
    )

    it should "correctly fold the values of neighbours with fold" in:
      foldingResult shouldBe 2
    it should "correctly fold the values of neighbours excluding self with nfold" in:
      neighbouringFoldingResult shouldBe 1

    it should "correctly fold the values of neighbours with fold when there are multiple neighbours" in:
      exportProbe = probe(
        localId = 3,
        factory = factory,
        program = foldingProgram,
        inboundMessages = Map(
          2 -> exportProbe(3),
          7 -> probe(
            localId = 7,
            factory = factory,
            program = foldingProgram,
            inboundMessages = Map(7 -> ValueTree.empty, 3 -> ValueTree.empty),
          )(3),
        ),
      )
      foldingResult shouldBe 42
    it should "correctly fold the values of neighbours excluding self with nfold when there are multiple neighbours" in:
      neighbouringFoldingResult shouldBe 14
  end foldingSemantics
end FoldingTests
