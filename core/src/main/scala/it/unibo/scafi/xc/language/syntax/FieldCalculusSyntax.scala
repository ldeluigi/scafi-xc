package it.unibo.scafi.xc.language.syntax

import it.unibo.scafi.xc.language.foundation.AggregateFoundation

trait FieldCalculusSyntax:
  self: AggregateFoundation =>

  /**
   * `nbr` sends a local value to <b>neighbors</b> and returns the aggregate value of the received messages.
   * @param expr
   *   the local value to send to neighbors
   * @tparam A
   *   the type of the local value
   * @return
   *   the aggregate value of the received messages
   */
  def nbr[A](expr: A): AggregateValue[A]

  /**
   * `rep` <b>repeatedly</b> applies a function to an initial value for every execution round.
   * @param init
   *   the initial value
   * @param f
   *   the function to apply
   * @tparam A
   *   the type of the value
   * @return
   *   the value after the last application of the function
   */
  def rep[A](init: A)(f: A => A): A

  /**
   * `share` computes a value by repeatedly applying a function to an initial value while <b>sharing</b> the result with
   * neighbors.
   * @param init
   *   the initial value
   * @param f
   *   the function that returns the value to share and return
   * @tparam A
   *   the type of the value
   * @return
   *   the value after the last application of the function that has been shared with neighbors
   */
  def share[A](init: A)(f: AggregateValue[A] => A): A
end FieldCalculusSyntax
