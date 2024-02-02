package it.unibo.scafi.xc.extensions.language.semantics.exchange

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }
import it.unibo.scafi.xc.extensions.language.AggregateFoundation
import it.unibo.scafi.xc.extensions.language.syntax._

trait ExchangeCalculusSemantics extends AggregateFoundation:
  type ID
  given idEquality: CanEqual[ID, ID] = CanEqual.derived

  override type AggregateValue[T] = NValues[ID, T]

  /**
   * This operator branches the computation into `th` or `el` according to `cond`.
   */
  protected def xcbranch[T](cond: AggregateValue[Boolean])(th: => AggregateValue[T])(
      el: => AggregateValue[T],
  ): AggregateValue[T]

  /**
   * This single operator handles state and message reception/sending.
   *
   * @param init
   *   initial value for new devices
   * @param f
   *   function from neighbouring value to neighbouring value
   * @tparam T
   *   the type of neighbouring values
   * @return
   *   the neighbouring value providing for the next local state and messages for neighbours
   */
  protected def xcexchange[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T]

  def self: ID
  def neighbors: AggregateValue[ID]

  override def lift: Liftable[AggregateValue] = NValues.given_Liftable_NValues

  override def fold: Foldable[AggregateValue] = NValues.given_Foldable_NValues

  override def convert[T]: Conversion[T, NValues[ID, T]] = NValues.given_Conversion_V_NValues

  extension [T](av: NValues[ID, T])
    override def onlySelf: T = av(self)
    override def withoutSelf: NValues[ID, T] = av.copy(values = av.values.filterKeys(_ != self))

end ExchangeCalculusSemantics

object ExchangeCalculusSemantics:

  given ExchangeCalculusSyntax[ExchangeCalculusSemantics] with

    extension (language: ExchangeCalculusSemantics)

      override def exchange[T](initial: language.AggregateValue[T])(
          f: language.AggregateValue[T] => (language.AggregateValue[T], language.AggregateValue[T]) |
            language.AggregateValue[T],
      ): language.AggregateValue[T] =
        language.xcexchange(initial)(n =>
          f(n) match
            case retSend: language.AggregateValue[T] => (retSend, retSend)
            case (ret, send) => (ret, send),
        )

  given ExpressiveFieldCalculusSyntax[ExchangeCalculusSemantics] with

    extension (language: ExchangeCalculusSemantics)

      override def rep[A](init: => language.AggregateValue[A])(
          f: language.AggregateValue[A] => language.AggregateValue[A],
      ): language.AggregateValue[A] =
        language.exchange(init)(prev => f(prev.onlySelf))

      override def nbr[A](expr: => language.AggregateValue[A]): language.AggregateValue[A] =
        language.exchange(expr)(n => (n, expr))

      override def share[A](init: => language.AggregateValue[A])(
          f: language.AggregateValue[A] => language.AggregateValue[A],
      ): language.AggregateValue[A] =
        language.exchange(init.onlySelf)(f)

  given BranchingSyntax[ExchangeCalculusSemantics] with

    extension (language: ExchangeCalculusSemantics)

      override def branch[T](cond: NValues[language.ID, Boolean])(th: => NValues[language.ID, T])(
          el: => NValues[language.ID, T],
      ): NValues[language.ID, T] =
        language.xcbranch(cond)(th)(el)

  given classicSyntax: ClassicFieldCalculusSyntax[ExchangeCalculusSemantics] with

    extension (language: ExchangeCalculusSemantics)

      override def nbr[V](expr: => NValues[language.ID, V]): NValues[language.ID, V] =
        summon[ExpressiveFieldCalculusSyntax[ExchangeCalculusSemantics]]
          .nbr(language)(expr)

      override def rep[A](init: => A)(f: A => A): A =
        summon[ExpressiveFieldCalculusSyntax[ExchangeCalculusSemantics]]
          .rep(language)(init)(nv => nv.map(f))
          .onlySelf

      override def share[A](init: => A)(f: A => A): A =
        summon[ExpressiveFieldCalculusSyntax[ExchangeCalculusSemantics]]
          .share(language)(init)(nv => nv.map(f))
          .onlySelf
end ExchangeCalculusSemantics
