package it.unibo.scafi.xc.collections

import it.unibo.scafi.xc.UnitTest

class SafeIterableDefaultTests extends UnitTest:

  "SafeIterable" should "provide a default implementation" in:
    SafeIterable(List(1, 2, 3)) shouldBe a[SafeIterable[Int]]
