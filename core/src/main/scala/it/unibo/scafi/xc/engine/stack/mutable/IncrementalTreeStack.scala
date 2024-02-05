package it.unibo.scafi.xc.engine.stack.mutable

import scala.annotation.targetName

class IncrementalTreeStack[Token, Value](using canEqual: CanEqual[Token, Token]) extends TreeStack[(Token, Int), Value]:

  @targetName("incrementalScope")
  def scope[T](token: Token)(body: () => (T, Option[Value])): T =
    val max = currentChildren.filter(_._1 == token).map(_._2).maxOption.getOrElse(0)
    scope(token, max + 1)(body)
