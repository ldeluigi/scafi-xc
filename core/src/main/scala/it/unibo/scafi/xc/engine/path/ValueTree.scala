package it.unibo.scafi.xc.engine.path

trait ValueTree[Token, Value] extends Iterable[(Path[Token], Value)] with PartialFunction[Path[Token], Value]:
  def hasPrefix(prefix: Path[Token]): Boolean
