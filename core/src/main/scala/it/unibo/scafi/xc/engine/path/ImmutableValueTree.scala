package it.unibo.scafi.xc.engine.path

import scala.collection.MapView

case class ImmutableValueTree[Token, Value](
    value: Option[Value] = Option.empty,
    private val childrenMap: Map[Token, ImmutableValueTree[Token, Value]] = Map.empty,
)(using CanEqual[Token, Token])
    extends ValueTree[Token, Value]
    with RecursiveValueTreeOps[Token, Value]:
  override def children: MapView[Token, RecursiveValueTreeOps[Token, Value]] = childrenMap.view
