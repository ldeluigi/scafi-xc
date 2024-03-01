package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.abstractions.Liftable.lift
import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.FieldCalculusSyntax

import FieldCalculusLibrary.nbr as fcNbr
import FoldingLibrary.nfold

object FoldhoodLibrary:

  /**
   * The nbr construct is used to access the corresponding values of the neighbors during the evaluation of a foldhood
   * expression.
   * @param expr
   *   the expression to be evaluated, once for self and once for each neighbor
   * @param c
   *   the foldhood context
   * @tparam A
   *   the type of the expression
   * @return
   *   the value of the expression
   */
  def nbr[A](expr: => A)(using c: FoldhoodContext): A = c.current(expr)

  /**
   * The foldhood construct is used to aggregate the results of an expression over the neighborhood, excluding self.
   * @param base
   *   the initial value of the aggregation
   * @param f
   *   the aggregation function
   * @param expr
   *   the expression to be evaluated, once for self and once for each neighbor
   * @param language
   *   the language context
   * @tparam A
   *   the type of the expression
   * @tparam B
   *   the type of the aggregation
   * @return
   *   the aggregated value
   */
  def foldhood[A, B](base: B)(f: (B, A) => B)(expr: FoldhoodContext ?=> A)(using
      language: AggregateFoundation & FieldCalculusSyntax,
  ): B = foldhoodImpl(false)(base)(f)(expr)

  /**
   * The foldhoodPlus construct is used to aggregate the results of an expression over the neighborhood, including self.
   * @param base
   *   the initial value of the aggregation
   * @param f
   *   the aggregation function
   * @param expr
   *   the expression to be evaluated, once for self and once for each neighbor
   * @param language
   *   the language context
   * @tparam A
   *   the type of the expression
   * @tparam B
   *   the type of the aggregation
   * @return
   *   the aggregated value
   */
  def foldhoodPlus[A, B](base: B)(f: (B, A) => B)(expr: FoldhoodContext ?=> A)(using
      language: AggregateFoundation & FieldCalculusSyntax,
  ): B = foldhoodImpl(true)(base)(f)(expr)

  private def foldhoodImpl[A, B](withSelf: Boolean)(base: B)(f: (B, A) => B)(expr: FoldhoodContext ?=> A)(using
      language: AggregateFoundation & FieldCalculusSyntax,
  ): B =
    var neighbouringValues: List[language.AggregateValue[Any | Null]] = List.empty
    val initial = new FoldhoodContext():
      override def current[X](expr: => X): X =
        val value: X = expr
        neighbouringValues = fcNbr(value) :: neighbouringValues
        value
    var zippedNeighbouringValues: language.AggregateValue[List[Any | Null]] = fcNbr(List.empty[Any | Null])
    val selfExprValue: A = expr(using initial)
    for nv <- neighbouringValues do
      zippedNeighbouringValues = lift(zippedNeighbouringValues, nv)((list, value) => value :: list)
    zippedNeighbouringValues.nfold(if withSelf then f(base, selfExprValue) else base): (acc, values) =>
      val iterator = values.iterator
      val context = new FoldhoodContext:
        override def current[X](expr: => X): X = iterator.next match
          case x: X @unchecked => x
          case _ => throw new ClassCastException("Type mismatch")
      val result = expr(using context)
      f(acc, result)
  end foldhoodImpl

  sealed abstract class FoldhoodContext:
    private[FoldhoodLibrary] def current[A](expr: => A): A
end FoldhoodLibrary
