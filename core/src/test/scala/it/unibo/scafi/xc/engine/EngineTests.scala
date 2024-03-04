package it.unibo.scafi.xc.engine

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }
import it.unibo.scafi.xc.engine.context.{ Context, TestingNetwork }
import it.unibo.scafi.xc.engine.network.{ Export, Import }
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate

class EngineTests extends UnitTest:

  case class ContextMock(
      localId: Int,
      override val inboundMessages: Import[Int, ValueTree[InvocationCoordinate, Any]],
  ) extends Context[Int, ValueTree[InvocationCoordinate, Any]]:

    override def outboundMessages: Export[Int, ValueTree[InvocationCoordinate, Any]] = MapWithDefault(
      default = ValueTree.empty,
      underlying = Map(
        localId -> ValueTree.empty,
      ),
    )

  val network: TestingNetwork[Int, InvocationCoordinate, Any] = TestingNetwork(
    localId = 5,
    received = Map(3 -> ValueTree.empty),
  )

  var count: Int = 0

  def programCounter(using ContextMock): Int =
    count += 1
    count

  val sut: Engine[
    Int,
    Int,
    ValueTree[InvocationCoordinate, Any],
    TestingNetwork[Int, InvocationCoordinate, Any],
    ContextMock,
  ] = Engine(
    network = network,
    factory = n => ContextMock(n.localId, n.receive()),
    program = programCounter,
  )

  "Engine" should "fire the program exactly once per cycle and return the result each time" in:
    sut.cycle() shouldBe 1
    count shouldBe 1
    sut.cycle() shouldBe 2
    count shouldBe 2
    sut.cycleWhile(_ => false) shouldBe 3
    count shouldBe 3

  it should "send outbound messages to network" in:
    sut.cycle()
    network.sent.keySet shouldBe Set(5)
    network.sent(5).isEmpty shouldBe true

  it should "allow to cycle until a condition is met" in:
    sut.cycleWhile(x =>
      x.inboundMessages.keySet shouldBe Set(3)
      x.outboundMessages.keySet shouldBe Set(5)
      x.result < 5,
    ) shouldBe 5
    count shouldBe 5
end EngineTests
