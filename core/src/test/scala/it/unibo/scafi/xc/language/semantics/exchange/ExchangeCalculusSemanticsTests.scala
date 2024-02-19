package it.unibo.scafi.xc.language.semantics.exchange

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.language.foundation.AggregateFoundationMock

trait ExchangeCalculusSemanticsTests:
  this: UnitTest =>

  def nvalues[C <: ExchangeCalculusSemantics: ExchangeCalculusSemanticsMock](using lang: C): Unit =
    val list: List[Int] = List(1, 2, 3, 4, 5)
    val nv = lang.mockNValues(10, list)
    it should "provide the default value" in:
      nv.default shouldEqual 10
    it should "allow to retrieve a value map" in:
      val v: Map[lang.DeviceId, Int] = nv.values.toMap
      v.values.toList should contain theSameElementsAs list
    it should "allow to retrieve a value" in:
      nv.get(lang.self) shouldEqual list.head
      nv(lang.self) shouldEqual list(1)
      nv.get(lang.unalignedDeviceId) shouldEqual 10
      nv(lang.unalignedDeviceId) shouldEqual 10
    it should "allow to override a value" in:
      var newNv = nv.set(lang.self, 100)
      newNv.get(lang.self) shouldEqual 100
      newNv.get(lang.unalignedDeviceId) shouldEqual 10
      newNv = newNv.set(lang.unalignedDeviceId, 100)
      newNv.get(lang.unalignedDeviceId) shouldEqual 100
  end nvalues

  def exchangeCalculusSemantics[C <: ExchangeCalculusSemantics: ExchangeCalculusSemanticsMock](using lang: C): Unit =
    it should "provide conversion from local values to nvalues using the default value" in:
      "val _: lang.AggregateValue[Int] = 10" should compile
      val example: lang.AggregateValue[String] = "a"
      example(lang.self) shouldEqual "a"
      example(lang.unalignedDeviceId) shouldEqual "a"
    "NValues" should behave like nvalues[C]
end ExchangeCalculusSemanticsTests
