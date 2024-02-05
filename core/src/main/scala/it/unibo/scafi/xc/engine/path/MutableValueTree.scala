package it.unibo.scafi.xc.engine.path

import collection.mutable.ListBuffer

case class MutableValueTree[Token, Value](
    token: Token,
    value: Option[Value] = Option.empty,
    children: ListBuffer[MutableValueTree[Token, Value]] = ListBuffer.empty,
)(using CanEqual[Token, Token])
    extends ValueTree[Token, Value]:
  override def hasPrefix(prefix: Path[Token]): Boolean = ???

  override def isDefinedAt(x: Path[Token]): Boolean = ???

  override def iterator: Iterator[(Path[Token], Value)] = ???

  override def apply(v1: Path[Token]): Value = ???
