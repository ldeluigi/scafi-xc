package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.foundation.AggregateFoundation
import it.unibo.scafi.xc.language.syntax.common.RetSend

/**
 * This trait provides the syntax for the exchange calculus main construct: `exchange`.
 */
trait ExchangeCalculusSyntax:
  self: AggregateFoundation =>

  /**
   * This method is the main construct of the exchange calculus. It allows both to send and receive messages, and to
   * perform local computation. It allows to send an aggregate value as a message to neighbors, and to return a
   * different aggregate value as a result of the computation.
   *
   * <h3>Examples</h3>
   * @param initial
   *   the initial aggregate value
   * @param f
   *   the function that takes the initial/received aggregate value and returns a new aggregate value or two aggregate
   *   values, one to be sent and one to be returned
   * @tparam T
   *   the type of the aggregate value
   * @return
   *   the new aggregate value
   */
  def exchange[T](initial: AggregateValue[T])(
      f: AggregateValue[T] => RetSend[AggregateValue[T]],
  ): AggregateValue[T]
end ExchangeCalculusSyntax
