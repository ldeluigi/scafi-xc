package it.unibo.scafi.xc.tests.acceptance

import scala.collection.mutable

import it.unibo.scafi.xc.simulator.deterministic.Device
import it.unibo.scafi.xc.tests.networks.GridNetwork
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

class GradientWithObstacleTest extends AcceptanceTest with GridNetwork:
  override type TestProgramResult = Double
  val epsilon = 0.0001
  override def rows: Int = 10
  override def columns: Int = 10
  override def ticks: Int = 1600
  def isSource(id: PositionInGrid): Boolean = id.row == 0 && id.col == 0
  def isObstacle(id: PositionInGrid): Boolean = id.row > 0 && id.col == 4

  // Network:
  // s * * * * * * * * *
  // * * * * | * * * * *
  // * * * * | * * * * *
  // * * * * | * * * * *
  // * * * * | * * * * *
  // * * * * | * * * * *
  // * * * * | * * * * *
  // * * * * | * * * * *
  // * * * * | * * * * *
  // * * * * | * * * * *

  override def device(row: Int, col: Int): Device[PositionInGrid] =
    Device.WithFixedSleepTime(PositionInGrid(row, col), ((row + 1) * col % 3) + 1)

  override def program(using TestProgramContext): Double =
    val round = rep(0)(_ + 1)
    branch(isObstacle(self) && round >= 200)(Double.PositiveInfinity)(distanceTo(isSource(self), 1.0))

  "The gradient" should "never be calculated for the obstacle" in:
    results
      .filter(kv => isObstacle(kv._1))
      .foreach: (id, value) =>
        value shouldBe Double.PositiveInfinity

  it should "be calculated correctly with obstacles" in:
    results
      .filter(kv => !isObstacle(kv._1))
      .foreach: (id, value) =>
        if id.col < 4 || id.row == 0 then value shouldBe Math.max(id.row, id.col).toDouble +- epsilon
        else value shouldBe Math.max(id.row, id.col - 4).toDouble + 4 +- epsilon
end GradientWithObstacleTest
