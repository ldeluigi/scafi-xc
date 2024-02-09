package it.unibo.scafi.xc.abstractions

import scala.annotation.targetName

case class BidirectionalAdapter[A, B](forward: A => B, backward: B => A)

object BidirectionalAdapter:

  @targetName("bidirectionalAdapter")
  infix type <=>[A, B] = BidirectionalAdapter[A, B]
