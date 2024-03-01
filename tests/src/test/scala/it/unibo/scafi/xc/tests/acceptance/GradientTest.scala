package it.unibo.scafi.xc.tests.acceptance

import it.unibo.scafi.xc.simulator.deterministic.SleepingDevice
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

class GradientTest extends AcceptanceTest:
  override type TestDeviceId = Int
  override type TestProgramResult = Double
  override val ticks = 10

  override def network: Map[SleepingDevice[TestDeviceId], Set[TestDeviceId]] = Map(
    SleepingDevice.WithFixedSleepTime(id = 1, sleepTime = 2) -> Set(1, 2),
    SleepingDevice.WithFixedSleepTime(id = 2, sleepTime = 1) -> Set(1, 2, 3),
    SleepingDevice.WithFixedSleepTime(id = 3, sleepTime = 3) -> Set(2, 3),
  )

  override def program(using TestProgramContext): Double =
    distanceTo(self == 1, 1.0)

  "The hop distance" should "be calculated correctly" in:
    results(1) should be(0.0)
    results(2) should be(1.0)
    results(3) should be(2.0)
end GradientTest
