package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.{ ContextFactory, ProbingContextMixin, TestingNetwork }
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

trait BranchingTests:
  this: UnitTest & ProbingContextMixin & BasicFactoryMixin =>

  def branchingSemantics(): Unit =
    def branchingProgram(using BasicExchangeCalculusContext[Int]): Unit =
      branch(self % 2 == 0) {
        exchange(100)(x => x)
      } {
        exchange(200)(x => x)
      }
    val exportProbeEven: Export[Int, InvocationCoordinate, Any] = probe(
      localId = 142,
      factory = factory,
      program = branchingProgram,
    )
    val exportProbeOdd: Export[Int, InvocationCoordinate, Any] = probe(
      localId = 143,
      factory = factory,
      program = branchingProgram,
    )
    it should "not align branching domains" in:
      exportProbeEven(142).single._1.head shouldNot be(exportProbeOdd(143).single._1.head)
      exportProbeEven(142).single._2 should be(100)
      exportProbeOdd(143).single._2 should be(200)

    var neighborsCount = 0
    def branchingProgramWithSideEffect(using BasicExchangeCalculusContext[Int]): Unit =
      branch(self % 2 == 0) {
        exchange(100)(x =>
          neighborsCount = device.size
          x,
        )
      } {
        exchange(200)(x => x)
      }
    val crossingMessagesProbe: Export[Int, InvocationCoordinate, Any] = probe(
      localId = 0,
      factory = factory,
      program = branchingProgramWithSideEffect,
      inboundMessages = Map(
        143 -> exportProbeOdd(0),
        142 -> exportProbeEven(0),
      ),
    )

    it should "restrict domain to aligned neighbors" in:
      crossingMessagesProbe(0).single._2 should be(100)
      neighborsCount should be(2)
  end branchingSemantics
end BranchingTests
