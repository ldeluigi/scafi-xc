package it.unibo.scafi.xc.tests.networks

import it.unibo.scafi.xc.simulator.deterministic.Device
import it.unibo.scafi.xc.tests.DeterministicSimulatorBasedTest

trait GridNetwork:
  self: DeterministicSimulatorBasedTest =>
  case class PositionInGrid(row: Int, col: Int)
  override type TestDeviceId = PositionInGrid

  def rows: Int

  def columns: Int

  def device(row: Int, col: Int): Device[PositionInGrid]

  override def network: Map[Device[TestDeviceId], Set[TestDeviceId]] =
    Map.from(for
      row <- 0 until rows
      col <- 0 until columns
    yield device(row, col) -> Set.from(for
      row2 <- Math.max(row - 1, 0) to Math.min(row + 1, rows - 1)
      col2 <- Math.max(col - 1, 0) to Math.min(col + 1, columns - 1)
    yield PositionInGrid(row2, col2)))

end GridNetwork
