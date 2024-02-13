package it.unibo.scafi.xc.collections

import scala.collection.immutable

class MapWithDefault[K, +V](underlying: Map[K, V], val default: V)
    extends Map.WithDefault[K, V](underlying, _ => default):

  def map[V2](f: V => V2): MapWithDefault[K, V2] = new MapWithDefault(underlying.map((k, v) => k -> f(v)), f(default))

  override def toString(): String = mkString(s"$className(default = $default|", ", ", ")")

object MapWithDefault:
  def empty[K, V](default: V): MapWithDefault[K, V] = new MapWithDefault(Map.empty, default)
