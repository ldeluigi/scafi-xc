package it.unibo.scafi.xc.engine.path

import it.unibo.scafi.xc.engine.path.Path

type ValueTree[DeviceId, Token] = Map[Path[Token], Map.WithDefault[DeviceId, Any]]
