package it.unibo.scafi.xc.abstractions

trait SafeIterable[+T]:

  private val iterable: Iterable[T] = new Iterable[T]:
    override def iterator: Iterator[T] = SafeIterable.this.iterator

  protected def iterator: Iterator[T]

  def toIterable: Iterable[T] = iterable

  override def toString: String = iterable.toString()

  override def hashCode(): Int = iterable.hashCode()

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
    reduceLeftOption,
    reduceOption,
    reduceRightOption,
    size,
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
