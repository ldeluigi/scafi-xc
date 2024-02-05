package it.unibo.scafi.xc.engine.stack.mutable

import it.unibo.scafi.xc.engine.path.{ ImmutableValueTree, Path }

trait Stack[Token, Value]:
  def snapshot: ImmutableValueTree[Token, Value]

  def currentPath: Path[Token]
  def currentChildren: Iterable[Token]

  def scope[T](token: Token)(body: () => (T, Option[Value])): T
