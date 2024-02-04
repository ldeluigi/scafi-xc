package it.unibo.scafi.xc.language.semantics.exchange

import it.unibo.scafi.xc.abstractions.Foldable
import it.unibo.scafi.xc.language.foundation.{ AggregateFoundation, DeviceAwareAggregateFoundation }
import it.unibo.scafi.xc.language.semantics.exchange
import it.unibo.scafi.xc.language.semantics.exchange.syntaxes.{
  BranchingExchangeSemantics,
  ExchangeSemantics,
  FieldCalculusByExchangeSemantics,
}

trait ExchangeCalculusSemantics
    extends AggregateFoundation
    with DeviceAwareAggregateFoundation
    with ExchangeSemantics
    with BranchingExchangeSemantics
    with FieldCalculusByExchangeSemantics:

  /**
   * NValues are maps from aligned neighbouring device identifiers to values, with a default value.
   */
  override type AggregateValue[T] <: NValues[DeviceId, T]
  override type NeighbouringValue[T] <: NValuesWithoutSelf[DeviceId, T]

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

  override def fold: Foldable[NeighbouringValue] = NValuesWithoutSelf.given_Foldable_AV[DeviceId, NeighbouringValue]
end ExchangeCalculusSemantics
