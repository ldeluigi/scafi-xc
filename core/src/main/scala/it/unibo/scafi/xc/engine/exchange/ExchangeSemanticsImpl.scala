package it.unibo.scafi.xc.engine.exchange

//import it.unibo.scafi.xc.engine.stack.Stack
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait ExchangeSemanticsImpl:
  this: ExchangeCalculusSemantics with NValuesSemanticsImpl =>

//  private var stack = Stack[String]()

  override protected def br[T](cond: Boolean)(th: => T)(el: => T): T = ???

  override protected def xc[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T] = ???

  override def aligned: Set[DeviceId] = ???

  def previousState: ProtoPreviousStateType
