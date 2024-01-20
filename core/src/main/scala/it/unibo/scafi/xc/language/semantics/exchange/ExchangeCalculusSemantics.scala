package it.unibo.scafi.xc.language.semantics.exchange

import it.unibo.scafi.xc.language.Language
import it.unibo.scafi.xc.language.syntax.formal.ExchangeCalculusSyntax

trait ExchangeCalculusSemantics extends Language {
  type ID
  type NV[T] <: NValues[ID, T]
  override type Value[T] = NV[T]

  /**
   * This operator branches the computation into `th` or `el` according to `cond`.
   */
  protected def xcbranch[T](cond: NV[Boolean])(th: => NV[T])(el: => NV[T]): NV[T]

  /**
   * This single operator handles state and message reception/sending.
   *
   * @param init initial value for new devices
   * @param f function from neighbouring value to neighbouring value
   * @tparam T the type of neighbouring values
   * @return the neighbouring value providing for the next local state and messages for neighbours
   */
  protected def exchange[T](init: NV[T])(f: NV[T] => (NV[T], NV[T])): NV[T]
}

object ExchangeCalculusSemantics {
  given ExchangeCalculusSyntax[ExchangeCalculusSemantics] with {
    extension (language: ExchangeCalculusSemantics) override def exchange[T](initial: language.NV[T], f: language.NV[T] => (language.NV[T], language.NV[T])): language.NV[T] =
        language.exchange(initial)(f)
  }
}
