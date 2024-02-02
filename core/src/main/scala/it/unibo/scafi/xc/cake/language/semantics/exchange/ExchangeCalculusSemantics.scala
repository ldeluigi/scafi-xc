package it.unibo.scafi.xc.cake.language.semantics.exchange

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }
import it.unibo.scafi.xc.extensions.language.AggregateFoundation

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
