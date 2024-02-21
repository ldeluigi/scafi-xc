package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.{ ContextFactory, ProbingContextMixin, TestingNetwork }
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

trait ExchangeCalculusTests:
  this: UnitTest & ProbingContextMixin & BasicFactoryMixin =>

  def exchangeSemantics(): Unit =
    var neighbors: Set[Int] = Set.empty
    def exchangingProgram(using BasicExchangeCalculusContext[Int]): Unit =
      assert(exchange(self)(ids =>
        neighbors = ids.toSet
        ret(ids.map(_ + 1)) send ids,
      ).toSet == neighbors.map(_ + 1)) // assert the ret/send semantics
    var exportProbe: Export[Int, InvocationCoordinate, Any] = probe(
      localId = 142,
      factory = factory,
      program = exchangingProgram,
      inboundMessages = Map(1000 -> ValueTree.empty), // unaligned devices should be ignored
    )
    it should "exchange with self if the device is alone after reboot" in:
      neighbors shouldBe Set(142) // the device just rebooted and only sees themselves
      exportProbe.map(_._1) should contain(142)
      exportProbe(142).single._1.size shouldBe 1
    it should "exchange with self if the device is alone" in:
      exportProbe = probe(
        localId = 142,
        factory = factory,
        program = exchangingProgram,
        inboundMessages = Map(142 -> exportProbe(142)),
      )
      neighbors shouldBe Set(142) // the device is still alone
      exportProbe.single._1 shouldBe 142
      exportProbe(142).single._1.size shouldBe 1
    it should "exchange with neighbors" in:
      val messageForNewNeighbor: Export[Int, InvocationCoordinate, Any] = probe(
        localId = 142,
        factory = factory,
        program = exchangingProgram,
        inboundMessages = Map(142 -> exportProbe(142)),
      )
      exportProbe = probe(
        localId = 0,
        factory = factory,
        program = exchangingProgram,
        inboundMessages = Map(142 -> messageForNewNeighbor(0)),
      )
      neighbors shouldBe Set(0, 142)
      exportProbe = probe(
        localId = 0,
        factory = factory,
        program = exchangingProgram,
        inboundMessages = Map(0 -> exportProbe(0), 142 -> messageForNewNeighbor(0)),
      )
      neighbors shouldBe Set(0, 142) // the device sees themselves after self messaging (memory)
  end exchangeSemantics
end ExchangeCalculusTests
