package it.unibo.scafi.xc.engine.path

type Path[Token] = List[Token]

object Path:
  def empty[A]: Path[A] = Nil
  def from[B](coll: collection.IterableOnce[B]): List[B] = Nil.prependedAll(coll)
