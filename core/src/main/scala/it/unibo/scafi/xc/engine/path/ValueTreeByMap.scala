package it.unibo.scafi.xc.engine.path

case class ValueTreeByMap[Token, Value](map: Map[Path[Token], Value]) extends ValueTree[Token, Value]:
  override def hasPrefix(prefix: Path[Token]): Boolean = map.keys.exists(_.startsWith(prefix))

  override def isDefinedAt(x: Path[Token]): Boolean = map.isDefinedAt(x)

  override def iterator: Iterator[(Path[Token], Value)] = map.iterator

  override def apply(v1: Path[Token]): Value = map(v1)
