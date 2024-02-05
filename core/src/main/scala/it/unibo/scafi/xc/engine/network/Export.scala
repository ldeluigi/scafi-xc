package it.unibo.scafi.xc.engine.network

import it.unibo.scafi.xc.engine.path.MutableValueTree

type Export[DeviceId, Token] = MutableValueTree[Token, Map.WithDefault[DeviceId, Any]]
