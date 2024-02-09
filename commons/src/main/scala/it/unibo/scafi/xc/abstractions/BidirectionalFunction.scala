package it.unibo.scafi.xc.abstractions

import scala.annotation.targetName

case class BidirectionalFunction[A, B](forward: A => B, backward: B => A)

object BidirectionalFunction:

  @targetName("bidirectionalFunctionType")
  infix type <=>[A, B] = BidirectionalFunction[A, B]

  @targetName("bidirectionalFunction0")
  def <=>[A]: A <=> A = BidirectionalFunction[A, A](x => x, x => x)

  @targetName("bidirectionalFunction1")
  def <=>[A](forward: A => A): A <=> A = BidirectionalFunction[A, A](forward, x => x)

  @targetName("bidirectionalFunction2")
  def <=>[A, B](forward: A => B, backward: B => A): A <=> B = BidirectionalFunction[A, B](forward, backward)
