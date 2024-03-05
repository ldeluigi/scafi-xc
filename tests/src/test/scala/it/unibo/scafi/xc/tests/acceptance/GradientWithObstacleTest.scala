package it.unibo.scafi.xc.tests.acceptance

import scala.collection.mutable

import it.unibo.scafi.xc.simulator.deterministic.SleepingDevice
import it.unibo.scafi.xc.tests.networks.GridNetwork
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

class GradientWithObstacleTest extends AcceptanceTest with GridNetwork:
  override type TestProgramResult = Double
  val epsilon: Double = 0.0001
  val obstacleGradient: Double = Double.PositiveInfinity
  override def rows: Int = 10
  override def columns: Int = 10
  override def ticks: Int = 1600
  def isSource(id: PositionInGrid): Boolean = id.row == 0 && id.col == 0
  def isObstacle(id: PositionInGrid): Boolean = id.row > 0 && id.col == 4

  def expectedGradient(id: PositionInGrid): Double =
    if isObstacle(id) then obstacleGradient
    else if id.col < 4 || id.row == 0 then Math.max(id.row, id.col).toDouble
    else Math.max(id.row, id.col - 4).toDouble + 4

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

  override def device(row: Int, col: Int): SleepingDevice[PositionInGrid] =
    SleepingDevice.WithFixedSleepTime(PositionInGrid(row, col), ((row + 1) * col % 3) + 1)

  override def program(using TestProgramContext): Double =
    val round = rep(0)(_ + 1)
    branch(isObstacle(self) && round >= 200)(obstacleGradient)(distanceTo(isSource(self), 1.0))

  "The gradient" should "never be calculated for the obstacle" in:
    results
      .filter(kv => isObstacle(kv._1))
      .foreach: (id, value) =>
        value shouldBe obstacleGradient

  it should "be calculated correctly with obstacles" in:
    results.foreach: (id, value) =>
      value shouldBe expectedGradient(id) +- epsilon
end GradientWithObstacleTest
