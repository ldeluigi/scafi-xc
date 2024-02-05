package it.unibo.scafi.xc.engine.path

type Path[Token] = List[Token]

object Path:
  def apply[Token](tokens: Token*): Path[Token] = tokens.toList
  def empty[Token]: Path[Token] = List.empty
