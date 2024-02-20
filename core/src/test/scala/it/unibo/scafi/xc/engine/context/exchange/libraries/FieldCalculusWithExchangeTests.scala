package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.context.{ ContextFactory, ProbingContextMixin, TestingNetwork }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

trait FieldCalculusWithExchangeTests:
  this: UnitTest & ProbingContextMixin & WithBasicFactory =>

  def nbrSemantics(): Unit =
    var neighbors: Set[Int] = Set.empty
    def neighbouringProgram(using BasicExchangeCalculusContext[Int]): Unit =
      neighbors = nbr(self + 5).toSet

    var exportProbe: Export[Int, InvocationCoordinate, Any] = probe(
      localId = 66,
      factory = factory,
      program = neighbouringProgram,
    )

    it should "send the value to self without neighbors after boot" in:
      exportProbe.single._1 shouldBe 66
      exportProbe.single._2.single._2.as[Int] shouldBe 71
      neighbors shouldBe Set(71)

    it should "send the value to self without neighbors" in:
      exportProbe = probe(
        localId = 66,
        factory = factory,
        program = neighbouringProgram,
        inboundMessages = exportProbe,
      )
      exportProbe.single._1 shouldBe 66
      exportProbe.single._2.single._2.as[Int] shouldBe 71
      neighbors shouldBe Set(71)

    it should "send the value to self with neighbors" in:
      exportProbe = probe(
        localId = 1,
        factory = factory,
        program = neighbouringProgram,
        inboundMessages = Map(
          1 -> probe(
            localId = 1,
            factory = factory,
            program = neighbouringProgram,
          )(1),
          2 -> probe(
            localId = 2,
            factory = factory,
            program = neighbouringProgram,
            inboundMessages = Map(1 -> ValueTree.empty),
          )(1),
          3 -> probe(
            localId = 3,
            factory = factory,
            program = neighbouringProgram,
            inboundMessages = Map(1 -> ValueTree.empty),
          )(1),
        ),
      )
      exportProbe.size shouldBe 3
      exportProbe(1).single._2.as[Int] shouldBe 6
      neighbors shouldBe Set(6, 7, 8)
  end nbrSemantics

  def repSemantics(): Unit =
    var last: Int = 0
    def repeatingProgram(using BasicExchangeCalculusContext[Int]): Unit =
      last = rep(0)(_ + 2)

    var exportProbe: Export[Int, InvocationCoordinate, Any] = probe(
      localId = 66,
      factory = factory,
      program = repeatingProgram,
    )
    val messageFromNeighbor: Export[Int, InvocationCoordinate, Any] = Map(
      1 ->
        probe( // adding a neighbor should not alter the result
          localId = 1,
          factory = factory,
          program = repeatingProgram,
        )(66),
    )

    it should "start from init after boot" in:
      exportProbe.single._1 shouldBe 66
      exportProbe(66).single._2.as[Option[Int]] shouldBe Some(2)
      last shouldBe 2

    it should "remember the last value after every iteration, and send None to others" in:
      exportProbe = probe(
        localId = 66,
        factory = factory,
        program = repeatingProgram,
        inboundMessages = exportProbe.filter(_._1 == 66) ++ messageFromNeighbor,
      )
      exportProbe.keySet should contain(66)
      exportProbe(66).single._2.as[Option[Int]] shouldBe Some(4)
      exportProbe(1).single._2.as[Option[Int]] shouldBe None
      last shouldBe 4
      exportProbe = probe(
        localId = 66,
        factory = factory,
        program = repeatingProgram,
        inboundMessages = exportProbe.filter(_._1 == 66) ++ messageFromNeighbor,
      )
      exportProbe.keySet should contain(66)
      exportProbe(66).single._2.as[Option[Int]] shouldBe Some(6)
      exportProbe(1).single._2.as[Option[Int]] shouldBe None
      last shouldBe 6

    it should "restart after reboot" in:
      exportProbe = probe(
        localId = 66,
        factory = factory,
        program = repeatingProgram,
        inboundMessages = messageFromNeighbor,
      )
      exportProbe.keySet should contain(66)
      exportProbe(66).single._2.as[Option[Int]] shouldBe Some(2)
      exportProbe(1).single._2.as[Option[Int]] shouldBe None
      last shouldBe 2
  end repSemantics

  def shareSemantics(): Unit =
    var res: Int = 0
    def sharingProgram(using BasicExchangeCalculusContext[Int]): Unit =
      res = share(1)(_.sum)
    var exportProbe: Export[Int, InvocationCoordinate, Any] = probe(
      localId = 7,
      factory = factory,
      program = sharingProgram,
    )
    it should "start from init after boot" in:
      exportProbe.single._1 shouldBe 7
      exportProbe.single._2.single._2.as[Int] shouldBe 1
      res shouldBe 1
    it should "send the value to self without neighbors" in:
      exportProbe = probe(
        localId = 7,
        factory = factory,
        program = sharingProgram,
        inboundMessages = exportProbe,
      )
      exportProbe.single._1 shouldBe 7
      exportProbe.single._2.single._2.as[Int] shouldBe 1
      res shouldBe 1
    it should "send/receive values to/from neighbors" in:
      exportProbe = probe(
        localId = 7,
        factory = factory,
        program = sharingProgram,
        inboundMessages = exportProbe ++ Map(
          1 -> probe(
            localId = 1,
            factory = factory,
            program = sharingProgram,
            inboundMessages = Map(7 -> ValueTree.empty),
          )(7),
          2 -> probe(
            localId = 2,
            factory = factory,
            program = sharingProgram,
            inboundMessages = Map(7 -> ValueTree.empty),
          )(7),
          3 -> probe(
            localId = 3,
            factory = factory,
            program = sharingProgram,
            inboundMessages = Map(7 -> ValueTree.empty),
          )(7),
        ),
      )
      exportProbe.size shouldBe 4
      exportProbe(1).single._2.as[Int] shouldBe 4
      exportProbe(2).single._2.as[Int] shouldBe 4
      exportProbe = probe(
        localId = 7,
        factory = factory,
        program = sharingProgram,
        inboundMessages = exportProbe ++ Map(
          1 -> probe(
            localId = 1,
            factory = factory,
            program = sharingProgram,
            inboundMessages = Map(7 -> ValueTree.empty),
          )(7),
          2 -> probe(
            localId = 2,
            factory = factory,
            program = sharingProgram,
            inboundMessages = Map(7 -> ValueTree.empty),
          )(7),
          3 -> probe(
            localId = 3,
            factory = factory,
            program = sharingProgram,
            inboundMessages = Map(7 -> ValueTree.empty),
          )(7),
        ),
      )
      exportProbe.size shouldBe 4
      exportProbe(1).single._2.as[Int] shouldBe 7
      exportProbe(2).single._2.as[Int] shouldBe 7

  end shareSemantics

  def fieldCalculusSemantics(): Unit =
    "nbr" should behave like nbrSemantics()
    "rep" should behave like repSemantics()
    "share" should behave like shareSemantics()
end FieldCalculusWithExchangeTests
