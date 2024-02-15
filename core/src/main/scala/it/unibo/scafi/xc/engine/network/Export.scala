package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.collections.{ MapWithDefault, ValueTree }

type Export[DeviceId, Token, Value] = MapWithDefault[DeviceId, ValueTree[Token, Value]]
