package it.unibo.scafi.xc.implicitsinmethods.language.semantics.exchange

import it.unibo.scafi.xc.abstractions.{ Foldable, Liftable }
import it.unibo.scafi.xc.implicitsinmethods.language.AggregateFoundation
import it.unibo.scafi.xc.implicitsinmethods.language.syntax._

trait ExchangeCalculusSemantics[ID] extends AggregateFoundation[[V] =>> NValues[ID, V]] {
  type DeviceID = ID
  override type AggregateValue[T] = NValues[ID, T]
  given idEquality: CanEqual[ID, ID] = CanEqual.derived

  protected def xcbranch[T](cond: AggregateValue[Boolean])(th: => AggregateValue[T])(
      el: => AggregateValue[T],
  ): AggregateValue[T]

  protected def xcexchange[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T]

  def self: ID

  def neighbors: AggregateValue[ID]

  override def lift: Liftable[AggregateValue] = NValues.given_Liftable_NValues

  override def fold: Foldable[AggregateValue] = NValues.given_Foldable_NValues

  override def convert[T]: Conversion[T, AggregateValue[T]] = NValues.given_Conversion_V_NValues

  extension [T](av: NValues[ID, T]) {
    override def onlySelf: T = av(self)
    override def withoutSelf: NValues[ID, T] = av.copy(values = av.values.filterKeys(_ != self))
  }
}

object ExchangeCalculusSemantics {

  class ExchangeSyntaxImpl[ID](using
      ExchangeCalculusSemantics[ID],
  ) extends ExchangeCalculusSyntax[[V] =>> NValues[ID, V], ExchangeCalculusSemantics[ID]] {

    override def exchange[T](initial: NValues[ID, T])(
        f: NValues[ID, T] => (NValues[ID, T], NValues[ID, T]) | NValues[ID, T],
    ): NValues[ID, T] =
      summon[ExchangeCalculusSemantics[ID]].xcexchange(initial)(n =>
        f(n) match {
          case retSend: NValues[ID, T] => (retSend, retSend)
          case (ret, send) => (ret, send)
        },
      )
  }

  class BranchingSyntaxImpl[ID](using
      ExchangeCalculusSemantics[ID],
  ) extends BranchingSyntax[[V] =>> NValues[ID, V], ExchangeCalculusSemantics[ID]] {

    override def branch[T](cond: NValues[ID, Boolean])(th: => NValues[ID, T])(el: => NValues[ID, T]): NValues[ID, T] =
      summon[ExchangeCalculusSemantics[ID]].xcbranch(cond)(th)(el)
  }

  class FieldSyntaxImpl[ID](using
      ExchangeCalculusSemantics[ID],
  ) extends ExpressiveFieldCalculusSyntax[[V] =>> NValues[ID, V], ExchangeCalculusSemantics[ID]] {
    private val syntax = ExchangeSyntaxImpl[ID]()
    import syntax._

    override def nbr[V](expr: => NValues[ID, V]): NValues[ID, V] =
      exchange(expr)(n => (n, expr))

    override def rep[A](init: => NValues[ID, A])(f: NValues[ID, A] => NValues[ID, A]): NValues[ID, A] =
      exchange(init)(prev => f(prev.onlySelf))

    override def share[A](init: => NValues[ID, A])(f: NValues[ID, A] => NValues[ID, A]): NValues[ID, A] =
      exchange(init.onlySelf)(f)
  }

  class ClassicSyntaxImpl[ID](using
      val semantics: ExchangeCalculusSemantics[ID],
  ) extends ClassicFieldCalculusSyntax[[V] =>> NValues[ID, V], ExchangeCalculusSemantics[ID]] {
    private val syntax = FieldSyntaxImpl[ID]()

    override def nbr[V](expr: => NValues[ID, V]): NValues[ID, V] =
      syntax.nbr(expr)

    override def rep[A](init: => A)(f: A => A): A =
      syntax.rep(init)(nv => nv.map(f)).onlySelf

    override def share[A](init: => A)(f: A => A): A =
      syntax.share(init)(nv => nv.map(f)).onlySelf
  }
}
