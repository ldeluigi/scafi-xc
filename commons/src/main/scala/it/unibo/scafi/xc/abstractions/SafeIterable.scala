package it.unibo.scafi.xc.abstractions

import it.unibo.scafi.xc.abstractions.boundaries.{ LowerBounded, UpperBounded }

trait SafeIterable[+A]:

  private val iterable: Iterable[A] = new Iterable[A]:
    override def iterator: Iterator[A] = SafeIterable.this.iterator

  protected def iterator: Iterator[A]

  def toIterable: Iterable[A] = iterable

  override def toString: String = iterable.toString()

  override def hashCode(): Int = iterable.hashCode()

  def min[B >: A: Ordering: UpperBounded]: B = minOption.getOrElse(summon[UpperBounded[B]].upperBound)

  def max[B >: A: Ordering: LowerBounded]: B = maxOption.getOrElse(summon[LowerBounded[B]].lowerBound)

  export iterable.{
    collectFirst,
    copyToArray,
    corresponds,
    count,
    exists,
    find,
    fold,
    foldLeft,
    foldRight,
    forall,
    foreach,
    groupMapReduce,
    headOption,
    isEmpty,
    lastOption,
    maxByOption,
    maxOption,
    minByOption,
    minOption,
    mkString,
    nonEmpty,
    product,
    reduceLeftOption,
    reduceOption,
    reduceRightOption,
    size,
    sum,
    to,
    toArray,
    toBuffer,
    toIndexedSeq,
    toList,
    toMap,
    toSeq,
    toSet,
    toVector,
    view,
  }
end SafeIterable

object SafeIterable:

  def apply[T](underlying: Iterable[T]): SafeIterable[T] = new SafeIterable[T]:
    override protected def iterator: Iterator[T] = underlying.iterator
