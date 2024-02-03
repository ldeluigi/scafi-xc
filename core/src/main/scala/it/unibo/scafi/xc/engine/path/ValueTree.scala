package it.unibo.scafi.xc.engine.path

import it.unibo.scafi.xc.engine.path.Path

type ValueTree[Token, Value] = Map[Path[Token], Value]
