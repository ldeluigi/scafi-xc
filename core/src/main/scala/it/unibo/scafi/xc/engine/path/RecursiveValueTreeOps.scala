package it.unibo.scafi.xc.engine.path

import scala.collection.MapView

trait RecursiveValueTreeOps[Token, Value](using CanEqual[Token, Token]):
  self: ValueTree[Token, Value] =>
  def children: MapView[Token, RecursiveValueTreeOps[Token, Value]]

  def value: Option[Value]

  def get(path: Path[Token]): Option[Value] = path match
    case Nil => value
    case head :: tail => children.get(head).flatMap(_.get(tail))

  override def hasPrefix(prefix: Path[Token]): Boolean = prefix match
    case Nil => true
    case head :: tail => children.get(head).exists(_.hasPrefix(tail))

  override def isDefinedAt(x: Path[Token]): Boolean = get(x).isDefined

  override def apply(v1: Path[Token]): Value = get(v1).get

  override def iterator: Iterator[(Path[Token], Value)] =
    value.iterator.map((Nil, _)) ++ children.iterator.flatMap((k, v) => v.iterator.map((kk, vv) => (k :: kk, vv)))

end RecursiveValueTreeOps
