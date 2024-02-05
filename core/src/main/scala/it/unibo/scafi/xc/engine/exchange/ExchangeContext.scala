package it.unibo.scafi.xc.engine.exchange

import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

class ExchangeContext[Id](override val self: Id, override val previousState: ProtoPreviousStateType)
    extends ExchangeCalculusSemantics
    with NValuesSemanticsImpl
    with ExchangeSemanticsImpl:
  override type DeviceId = Id
