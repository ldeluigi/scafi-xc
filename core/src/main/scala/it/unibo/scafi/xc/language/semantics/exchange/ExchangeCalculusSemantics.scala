package it.unibo.scafi.xc.language.semantics.exchange

import it.unibo.scafi.xc.language.AggregateLanguage
import it.unibo.scafi.xc.language.semantics.exchange
import it.unibo.scafi.xc.language.syntax.formal.{ ExchangeCalculusSyntax, ExpressiveFieldCalculusSyntax }

trait ExchangeCalculusSemantics extends AggregateLanguage {
  type ID
  override type AggregateValue[T] = NValues[ID, T]

  given idEquality: CanEqual[ID, ID] = CanEqual.derived

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

  protected def self: ID

  extension [V](nv: NValues[ID, V]) {
    def onlySelf: NValues[ID, V] = nv.copy(values = nv.values.filter(_._1 == self))
  }
}

object ExchangeCalculusSemantics {

  given ExchangeCalculusSyntax[ExchangeCalculusSemantics] with {

    extension (language: ExchangeCalculusSemantics) {

      override def exchange[T](initial: language.AggregateValue[T])(
          f: language.AggregateValue[T] => (language.AggregateValue[T], language.AggregateValue[T]) |
            language.AggregateValue[T],
      ): language.AggregateValue[T] =
        language.xcexchange(initial)(n =>
          f(n) match {
            case retSend: language.AggregateValue[T] => (retSend, retSend)
            case (ret, send) => (ret, send)
          },
        )
    }
  }

  given ExpressiveFieldCalculusSyntax[ExchangeCalculusSemantics] with {

    extension (language: ExchangeCalculusSemantics) {

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
    }
  }
}
