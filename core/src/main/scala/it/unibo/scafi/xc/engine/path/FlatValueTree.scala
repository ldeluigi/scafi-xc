package it.unibo.scafi.xc.engine.path

case class FlatValueTree[Token, Value](private val fromMap: Map[Path[Token], Value]) extends ValueTree[Token, Value]:

  override def hasPrefix(prefix: Path[Token]): Boolean = fromMap.keys.exists(_.startsWith(prefix))

  override def isDefinedAt(x: Path[Token]): Boolean = fromMap.isDefinedAt(x)

  override def iterator: Iterator[(Path[Token], Value)] = fromMap.iterator

  override def apply(v1: Path[Token]): Value = fromMap(v1)
