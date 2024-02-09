package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.engine.path.ValueTree

type Export[DeviceId, Token, Value] = ValueTree[Token, MessageMap[DeviceId, Value]]
