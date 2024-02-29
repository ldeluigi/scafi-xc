package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.abstractions.Liftable.lift
import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

import FieldCalculusLibrary.nbr as fcNbr
import FoldingLibrary.nfold

object FoldhoodLibrary:
  def nbr[A](expr: => A)(using c: FoldhoodContext): A = c.current(expr)

  def foldhood[A, B](base: B)(f: (B, A) => B)(expr: FoldhoodContext ?=> A)(using
      language: AggregateFoundation & FieldCalculusSyntax,
  ): B = foldhoodImpl(false)(base)(f)(expr)

  def foldhoodPlus[A, B](base: B)(f: (B, A) => B)(expr: FoldhoodContext ?=> A)(using
      language: AggregateFoundation & FieldCalculusSyntax,
  ): B = foldhoodImpl(true)(base)(f)(expr)

  private def foldhoodImpl[A, B](withSelf: Boolean)(base: B)(f: (B, A) => B)(expr: FoldhoodContext ?=> A)(using
      language: AggregateFoundation & FieldCalculusSyntax,
  ): B =
    val initial = FoldhoodContext()
    val selfExprValue: A = expr(using initial)
    val selfValues: List[Any | Null] = initial.values
    val neighbouringValues: List[language.AggregateValue[Any | Null]] = for value <- selfValues yield fcNbr(value)
    var zippedNeighbouringValues: language.AggregateValue[List[Any | Null]] = fcNbr(List.empty[Any | Null])
    for nv <- neighbouringValues do
      zippedNeighbouringValues = lift(zippedNeighbouringValues, nv)((list, value) => value :: list)
    zippedNeighbouringValues.nfold(if withSelf then f(base, selfExprValue) else base): (acc, values) =>
      val result = expr(using FoldhoodContext(Some(values.iterator)))
      f(acc, result)

  sealed class FoldhoodContext private[FoldhoodLibrary] (neighborValues: Option[Iterator[Any | Null]] = None):
    private[FoldhoodLibrary] var values: List[Any | Null] = List.empty

    private def submit[A](expr: => A): A =
      val value: A = expr
      values = value :: values; value

    private[FoldhoodLibrary] def current[A](expr: => A): A = neighborValues.map(_.next).getOrElse(submit(expr)) match
      case x: A @unchecked => x
      case _ => throw new ClassCastException("Type mismatch")
end FoldhoodLibrary
