package it.unibo.scafi.xc.language.sensors

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.language.foundation.{ AggregateFoundation, AggregateFoundationMock }

import DistanceSensor.senseDistance

class DistanceSensorTests extends UnitTest:

  type Language = AggregateFoundation & DistanceSensor[Int]

  given lang: Language = new AggregateFoundationMock with DistanceSensor[Int]:
    override def senseDistance: MockAggregate[Int] = device.map(_ => 1)

  "senseDistance" should "be available from language" in:
    "val _: lang.AggregateValue[Int] = lang.senseDistance" should compile
    "val _: lang.AggregateValue[Double] = lang.senseDistance" shouldNot typeCheck

  it should "be callable statically" in:
    senseDistance[Int] should equal(lang.senseDistance)
