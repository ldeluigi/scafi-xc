package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.abstractions.Liftable.lift
import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

import FieldCalculusLibrary.nbr as fcNbr

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
    val selfValues: List[Any | Null] = initial.selfValues
    val neighbouringValues: List[language.AggregateValue[Any | Null]] = for value <- selfValues yield fcNbr(value)
    var tupledNeighbouringValues: language.AggregateValue[List[Any | Null]] = fcNbr(List.empty[Any | Null])
    for nv <- neighbouringValues do
      tupledNeighbouringValues = lift(tupledNeighbouringValues, nv)((list, value) => list :+ value)
    var finalResult: B = if withSelf then f(base, selfExprValue) else base
    for neighbourValues <- tupledNeighbouringValues.withoutSelf.toIterable do
      val result = expr(using FoldhoodContext(Some(neighbourValues.iterator)))
      finalResult = f(finalResult, result)
    finalResult

  sealed class FoldhoodContext private[FoldhoodLibrary] (neighborValues: Option[Iterator[Any | Null]] = None):
    private[FoldhoodLibrary] var selfValues: List[Any | Null] = List.empty

    private def submit[A](expr: => A): A =
      val value: A = expr
      selfValues = selfValues :+ value; value

    private[FoldhoodLibrary] def current[A](expr: => A): A = neighborValues.map(_.next).getOrElse(submit(expr)) match
      case x: A @unchecked => x
      case _ => throw new ClassCastException("Type mismatch")
end FoldhoodLibrary
