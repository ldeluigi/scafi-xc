package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.engine.path.ValueTree

type Import[DeviceId, Token] = Map[DeviceId, ValueTree[Token, Any]]
