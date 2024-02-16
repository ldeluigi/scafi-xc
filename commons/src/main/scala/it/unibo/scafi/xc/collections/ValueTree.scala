package it.unibo.scafi.xc.collections

import scala.annotation.targetName

import it.unibo.scafi.xc.implementations.collections.MapValueTree

trait ValueTree[N, +V] extends Iterable[(Seq[N], V)] with PartialFunction[Seq[N], V]:
  def contains(seq: Seq[N]): Boolean
  def containsPrefix(seq: Iterable[N]): Boolean
  def get(seq: Seq[N]): Option[V]
  def mapValues[V1](f: (Seq[N], V) => V1): ValueTree[N, V1]
  def mapNodes[N1](f: N => N1): ValueTree[N1, V]
  def map[N1, V1](f: (Seq[N], V) => (Seq[N1], V1)): ValueTree[N1, V1]
  def filter(f: (Seq[N], V) => Boolean): ValueTree[N, V]
  def filterNot(f: (Seq[N], V) => Boolean): ValueTree[N, V] = filter((k, v) => !f(k, v))
  def flatMap[N1, V1](f: (Seq[N], V) => IterableOnce[(Seq[N1], V1)]): ValueTree[N1, V1]
  def remove(seq: Seq[N]): ValueTree[N, V]
  def removePrefix(seq: Iterable[N]): ValueTree[N, V]
  def update[V1 >: V](seq: Seq[N], value: V1): ValueTree[N, V1]
  def concat[V1 >: V](other: ValueTree[N, V1]): ValueTree[N, V1]
  def partition(f: (Seq[N], V) => Boolean): (ValueTree[N, V], ValueTree[N, V])
  def prepend[N1 >: N](prefix: Seq[N1]): ValueTree[N1, V]
  def append[N1 >: N](suffix: Seq[N1]): ValueTree[N1, V]
  def reversedNodes: ValueTree[N, V]

  @targetName("concat")
  inline def ++[V1 >: V](other: ValueTree[N, V1]): ValueTree[N, V1] = concat(other)

  @targetName("update")
  inline def +[V1 >: V](kv: (Seq[N], V1)): ValueTree[N, V1] = update(kv._1, kv._2)

  override def className: String = "ValueTree"

  override def apply(v1: Seq[N]): V = get(v1).get

  override def isDefinedAt(v1: Seq[N]): Boolean = contains(v1)
end ValueTree

object ValueTree extends ValueTree.Factory[ValueTree]:

  trait Factory[VT[N, V] <: ValueTree[N, V]]:
    def empty[N, V]: VT[N, V]

    def apply[N, V](elems: (Seq[N], V)*): VT[N, V]

    @targetName("merge")
    def apply[N, V](elems: ValueTree[N, V]*): VT[N, V]

  override def empty[N, V]: ValueTree[N, V] = MapValueTree.empty

  override def apply[N, V](elems: (Seq[N], V)*): ValueTree[N, V] = MapValueTree(elems*)

  @targetName("merge")
  override def apply[N, V](elems: ValueTree[N, V]*): ValueTree[N, V] = MapValueTree(elems*)
