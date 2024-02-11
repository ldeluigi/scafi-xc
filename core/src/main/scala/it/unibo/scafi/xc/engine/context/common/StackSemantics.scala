package it.unibo.scafi.xc.engine.context.common

import scala.collection.{ mutable, IndexedSeqView }

import it.unibo.scafi.xc.engine.path.Path

trait StackSemantics:
  this: MessageSemantics =>
  private val trace: mutable.Map[Path[InvocationCoordinate], Int] = mutable.Map.empty[Path[InvocationCoordinate], Int]
  private val stack: mutable.Stack[InvocationCoordinate] = mutable.Stack.empty[InvocationCoordinate]

  protected def currentPath: IndexedSeqView[InvocationCoordinate] = stack.view.reverse

  protected def scope[T](key: String)(body: () => T): T =
    val next = trace.get(stack.toList).map(_ + 1).getOrElse(0)
    stack.push(InvocationCoordinate(key, next))
    val result = body()
    val _ = stack.pop()
    val _ = trace.put(stack.toList, next)
    result
end StackSemantics
