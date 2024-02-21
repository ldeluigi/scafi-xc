package it.unibo.scafi.xc.tests.acceptance

import it.unibo.scafi.xc.simulator.deterministic.Device
import it.unibo.scafi.xc.language.libraries.All.{ _, given }

class GradientTest extends AcceptanceTest:
  override val ticks = 10

  override val network: Map[Device[DeviceId], Set[DeviceId]] = Map(
    Device.WithFixedSleepTime(id = 1, sleepTime = 2) -> Set(1, 2),
    Device.WithFixedSleepTime(id = 2, sleepTime = 1) -> Set(1, 2, 3),
    Device.WithFixedSleepTime(id = 3, sleepTime = 3) -> Set(2, 3),
  )

  var results: Map[DeviceId, Double] = Map.empty
  override def cleanup(): Unit = results = Map.empty

  override def program(using C): Unit =
    results += self -> distanceTo(self == 1, 1.0)

  "The hop distance" should "be calculated correctly" in:
    results(1) should be(0.0)
    results(2) should be(1.0)
    results(3) should be(2.0)
end GradientTest
