package it.unibo.scafi.xc.language.semantics.exchange.syntaxes

import it.unibo.scafi.xc.language.foundation.DistributedSystemUtilities.Shareable
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.language.syntax.{ ExchangeCalculusSyntax, FieldCalculusSyntax }
import it.unibo.scafi.xc.language.syntax.common.RetSend.ret

/**
 * This trait witnesses the fact that the field calculus can be implemented by the exchange calculus.
 */
trait FieldCalculusByExchangeSemantics extends FieldCalculusSyntax:
  this: ExchangeCalculusSemantics & ExchangeCalculusSyntax =>

  override def nbr[V: Shareable](expr: V): AggregateValue[V] =
    exchange(expr)(nv => ret(nv) send expr)

  override def rep[A: Shareable](init: A)(f: A => A): A =
    exchange[Option[A]](None)(nones =>
      val previousValue = nones(self).getOrElse(init)
      nones.set(self, Some(f(previousValue))),
    ).get(self).get

  override def share[A: Shareable](init: A)(f: AggregateValue[A] => A): A =
    exchange(init)(nv => f(nv)).get(self)
