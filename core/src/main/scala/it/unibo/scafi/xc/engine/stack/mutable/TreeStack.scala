package it.unibo.scafi.xc.engine.stack.mutable

import scala.collection.mutable

import it.unibo.scafi.xc.engine.path.{ ImmutableValueTree, MutableValueTree, Path }

class TreeStack[Token, Value](using canEqual: CanEqual[Token, Token]) extends Stack[Token, Value]:
  private val tree = MutableValueTree[Token, Value]()
  private val stack = mutable.Stack[Token]()

  @SuppressWarnings(Array("DisableSyntax.var"))
  private var currentTree = tree

  override def snapshot: ImmutableValueTree[Token, Value] = currentTree.snapshot
  override def currentPath: Path[Token] = stack.toList
  override def currentChildren: Iterable[Token] = currentTree.children.keys

  override def scope[T](token: Token)(body: () => (T, Option[Value])): T =
    stack.push(token)
    val oldTree = currentTree
    currentTree = currentTree.child(token)
    val (result, value) = body()
    currentTree.value = value
    currentTree = oldTree
    val _ = stack.pop()
    result
end TreeStack
