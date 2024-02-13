package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.engine.path.ValueTree

type Import[DeviceId, Token, Value] = MapWithDefault[DeviceId, ValueTree[Token, Value]]
