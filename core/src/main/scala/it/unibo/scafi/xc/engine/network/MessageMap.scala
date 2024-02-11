package it.unibo.scafi.xc.engine.network

import scala.collection.immutable

class MessageMap[K, +V](underlying: Map[K, V], val default: V) extends Map.WithDefault[K, V](underlying, _ => default):

  def map[V2](f: V => V2): MessageMap[K, V2] = new MessageMap(underlying.map((k, v) => k -> f(v)), f(default))

object MessageMap:
  def empty[K, V](default: V): MessageMap[K, V] = new MessageMap(Map.empty, default)
