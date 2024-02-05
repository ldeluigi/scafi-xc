package it.unibo.scafi.xc.engine.path

import it.unibo.scafi.xc.engine.path.Path

type ValueTree[Token, Value] = Map[Path[Token], Value]

object ValueTree:

  extension [Token](tree: ValueTree[Token, _])
    def hasPrefix(prefix: Path[Token]): Boolean = tree.keys.exists(_.startsWith(prefix))
