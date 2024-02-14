package it.unibo.scafi.xc.engine.path

type ValueTree[Token, Value] = Map[Path[Token], Value]

object ValueTree:

  def empty[Token, Value]: ValueTree[Token, Value] = Map.empty
