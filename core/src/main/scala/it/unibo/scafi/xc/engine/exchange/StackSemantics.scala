package it.unibo.scafi.xc.engine.exchange

import scala.collection.{ mutable, IndexedSeqView }

trait StackSemantics:
  type Envelope
  protected def open[T](a: Envelope): T
  protected def close[T](a: T): Envelope

  case class InvocationCoordinate(key: String, index: Int)

  @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
  private var lastPopped = InvocationCoordinate("", 0)
  private val stack: mutable.Stack[InvocationCoordinate] = mutable.Stack.empty[InvocationCoordinate]

  protected def currentPath: IndexedSeqView[InvocationCoordinate] = stack.view.reverse

  protected def scope[T](key: String)(body: () => T): T =
    val next = if lastPopped.key == key then lastPopped.index + 1 else 0
    stack.push(InvocationCoordinate(key, next))
    val result = body()
    lastPopped = stack.pop()
    result
end StackSemantics
