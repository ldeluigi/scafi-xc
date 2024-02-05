package it.unibo.scafi.xc.engine.stack

import it.unibo.scafi.xc.engine.path.Path

class TreeStack[Token, Value](root: Token):

//  private var tree = MutableValueTree[Token, Value](root)
  @SuppressWarnings(Array("DisableSyntax.var"))
  var current: Path[Token] = Path[Token](root)
//  def snapshot: FlatValueTree[Token, Value] = tree.snapshot
