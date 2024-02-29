package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.engine.context.ProbingContextMixin
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.language.libraries.FoldhoodLibrary.{ foldhood, foldhoodPlus, nbr }
import it.unibo.scafi.xc.language.libraries.CommonLibrary.self
import it.unibo.scafi.xc.engine.network.{ Export, Import }

trait FoldhoodLibraryTests:
  this: UnitTest & ProbingContextMixin & BasicFactoryMixin =>

  def foldhoodSemantics(): Unit =
    var results: Map[Int, Int] = Map.empty

    def foldhoodingPlusProgram(using BasicExchangeCalculusContext[Int]): Unit =
      val foldhoodResult = foldhoodPlus[Int, Int](0)(_ + _) { nbr(self) + nbr("3").toInt + 1 }
      results += (self -> foldhoodResult)

    def foldhoodingProgram(using BasicExchangeCalculusContext[Int]): Unit =
      val foldhoodResult = foldhood[Int, Int](0)(_ + _) { nbr(self) + nbr("3").toInt + 1 }
      results += (self -> foldhoodResult)

    var exportProbe: Export[Int, InvocationCoordinate, Any] = probe(
      localId = 66,
      factory = factory,
      program = foldhoodingPlusProgram,
    )

    it should "evaluate foldhoodPlus expression only for self without neighbors" in:
      exportProbe.single._1 shouldBe 66
      results(66) shouldBe 70

    it should "evaluate foldhoodPlus expression for self and neighbors" in:
      exportProbe = probe(
        localId = 66,
        factory = factory,
        program = foldhoodingPlusProgram,
        inboundMessages = Map(
          1 -> probe(
            localId = 1,
            factory = factory,
            program = foldhoodingPlusProgram,
          )(66),
          2 -> probe(
            localId = 2,
            factory = factory,
            program = foldhoodingPlusProgram,
          )(66),
          3 -> probe(
            localId = 3,
            factory = factory,
            program = foldhoodingPlusProgram,
          )(66),
          66 -> exportProbe(66),
        ),
      )
      exportProbe.size shouldBe 4
      results(66) shouldBe 88

    it should "not evaluate foldhood expression for self without neighbors" in:
      exportProbe = probe(
        localId = 66,
        factory = factory,
        program = foldhoodingProgram,
      )
      exportProbe.single._1 shouldBe 66
      results(66) shouldBe 0

    it should "evaluate foldhood expression for self and neighbors" in:
      exportProbe = probe(
        localId = 66,
        factory = factory,
        program = foldhoodingProgram,
        inboundMessages = Map(
          1 -> probe(
            localId = 1,
            factory = factory,
            program = foldhoodingProgram,
          )(66),
          2 -> probe(
            localId = 2,
            factory = factory,
            program = foldhoodingProgram,
          )(66),
          3 -> probe(
            localId = 3,
            factory = factory,
            program = foldhoodingProgram,
          )(66),
          66 -> probe(
            localId = 66,
            factory = factory,
            program = foldhoodingProgram,
          )(66),
        ),
      )
      exportProbe.size shouldBe 4
      results(66) shouldBe 18
  end foldhoodSemantics
end FoldhoodLibraryTests
