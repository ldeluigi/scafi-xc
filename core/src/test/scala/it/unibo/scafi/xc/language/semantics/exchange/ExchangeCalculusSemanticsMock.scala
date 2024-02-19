package it.unibo.scafi.xc.language.semantics.exchange

trait ExchangeCalculusSemanticsMock[C <: ExchangeCalculusSemantics]:

  extension (lang: C)
    def mockNValues[T](default: T, values: Iterable[T]): lang.AggregateValue[T]
    def unalignedDeviceId: lang.DeviceId
