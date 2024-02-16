package it.unibo.scafi.xc.implementations

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.abstractions.BoundedTests
import it.unibo.scafi.xc.implementations.CommonBoundaries.given

class CommonBoundariesTests extends UnitTest with BoundedTests:
  "Double" should behave like bounded[Double]()
  "Float" should behave like bounded[Float]()
