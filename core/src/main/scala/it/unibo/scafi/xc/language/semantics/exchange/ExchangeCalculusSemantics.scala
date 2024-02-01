package it.unibo.scafi.xc.language.semantics.exchange

import it.unibo.scafi.xc.language.DeviceAwareAggregateFoundation

trait ExchangeCalculusSemantics extends DeviceAwareAggregateFoundation {

  /**
   * NValues are maps from aligned neighbouring device identifiers to values, with a default value.
   */
  trait NValues[T]
  override type AggregateValue[T] = NValues[T]

  /**
   * This operator branches the computation into `th` or `el` according to `cond`.
   */
  protected def br[T](cond: Boolean)(th: => T)(el: => T): T

  /**
   * This single operator handles state and message reception/sending.
   *
   * @param init
   *   initial value for new devices
   * @param f
   *   function from neighbouring value to the couple (new local state, message to send)
   * @tparam T
   *   the type of neighbouring values
   * @return
   *   the neighbouring value providing for the next local state
   */
  protected def xc[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T]
}
