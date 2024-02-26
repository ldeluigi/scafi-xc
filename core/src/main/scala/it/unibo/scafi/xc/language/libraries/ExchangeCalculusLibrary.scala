package it.unibo.scafi.xc.language.libraries

import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.ExchangeCalculusSyntax
import it.unibo.scafi.xc.language.syntax.common.RetSend

/**
 * This library provides the exchange calculus primitive, `exchange`.
 */
object ExchangeCalculusLibrary:
  export RetSend.{ *, given }

  /**
   * This method is the main construct of the exchange calculus. It allows both to send and receive messages, and to
   * perform local computation. It allows to send an aggregate value as a message to neighbours, and to return a
   * different aggregate value as a result of the computation.
   *
   * <h3>Examples</h3>
   *
   * <h4>To send and return the same value</h4> {{{exchange(0)(value => f(value))}}}
   * {{{exchange(0)(value => retsend(f(value)))}}} <h4>To send and return different values</h4>
   * {{{exchange(0)(value => (f(value), f2(value)))}}} {{{exchange(0)(value => ret (f(value)) send f2(value))}}}
   * {{{exchange(0)(value => ret(f(value)).send(f2(value)))}}} {{{exchange(0)(value => RetSend(f(value), f2(value)))}}}
   * @param initial
   *   the initial aggregate value
   * @param f
   *   the function that takes the initial/received aggregate value and returns a new aggregate value or two aggregate
   *   values, one to be sent and one to be returned
   * @tparam T
   *   the type of the aggregate value
   * @return
   *   the new aggregate value
   * @see
   *   [[RetSend]] [[ExchangeCalculusSyntax.exchange]]
   */
  def exchange[T: language.Shareable](using language: AggregateFoundation & ExchangeCalculusSyntax)(
      initial: language.AggregateValue[T],
  )(
      f: language.AggregateValue[T] => RetSend[language.AggregateValue[T]],
  ): language.AggregateValue[T] = language.exchange(initial)(f)
end ExchangeCalculusLibrary
