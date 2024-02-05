package it.unibo.scafi.xc.engine.path

import scala.collection.MapView
import scala.collection.mutable

case class MutableValueTree[Token, Value](
    @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
    var value: Option[Value] = Option.empty,
    private val childrenMap: mutable.Map[Token, MutableValueTree[Token, Value]] =
      mutable.Map.empty[Token, MutableValueTree[Token, Value]],
)(using CanEqual[Token, Token])
    extends ValueTree[Token, Value]
    with RecursiveValueTreeOps[Token, Value]:
  override def children: MapView[Token, RecursiveValueTreeOps[Token, Value]] = childrenMap.view

  def snapshot: ImmutableValueTree[Token, Value] =
    ImmutableValueTree(value, childrenMap.view.mapValues(_.snapshot).toMap)

  def child(token: Token, value: Option[Value]): MutableValueTree[Token, Value] =
    childrenMap.getOrElseUpdate(token, MutableValueTree(value))

  def child(token: Token): MutableValueTree[Token, Value] = child(token, None)
