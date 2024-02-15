package it.unibo.scafi.xc.implementations

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.abstractions.BoundedTests
import it.unibo.scafi.xc.implementations.CommonBoundaries.given

class CommonBoundariesTests extends UnitTest with BoundedTests:
  "Double" should behave like upperBounded[Double]()
  it should behave like lowerBounded[Double]()
  it should behave like bounded[Double]()

  "Float" should behave like upperBounded[Float]()
  it should behave like lowerBounded[Float]()
  it should behave like bounded[Float]()
