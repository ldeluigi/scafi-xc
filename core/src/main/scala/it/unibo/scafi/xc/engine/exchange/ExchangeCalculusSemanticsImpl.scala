package it.unibo.scafi.xc.engine.exchange

import scala.collection.MapView

import it.unibo.scafi.xc.engine.network.{ Export, Import }
import it.unibo.scafi.xc.engine.path.MutableValueTree
import it.unibo.scafi.xc.engine.path.MutableValueTree.*
import it.unibo.scafi.xc.engine.stack.TreeStack
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait ExchangeCalculusSemanticsImpl:
  this: ExchangeCalculusSemantics with NValuesSemanticsImpl =>

  private val stack = TreeStack[String, Nothing]("root")

  override def aligned: Set[DeviceId] = inboundMessages.view.filter(_._2.hasPrefix(stack.current)).keys.toSet

  private def alignedMessages[T]: MapView[DeviceId, T] =
    inboundMessages.view.filterKeys(aligned).mapValues(v => as[T](v(stack.current)))

  def previousValue[T](or: => T): T = ??? // as[T](previousState.getOrElse(stack.current, or))

  private def as[T](x: Any): T = x match
    case x: T @unchecked => x
    case _ => throw new IllegalStateException(s"Previous value is not of type ${x.getClass}")

  private def scoped[T](pivot: String)(body: () => T): T = ???
//    stack = stack.align(pivot)
//    val result = body()
//    stack = stack.dealign
//    result

  override protected def br[T](cond: Boolean)(th: => T)(el: => T): T = ???

  override protected def xc[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T] = scoped("exchange"): () =>
    val messages = alignedMessages[T]
    val previous = previousValue[T](or = init.onlySelf)
    val subject = NValuesImpl[T](previous, messages.toMap)
    val (ret, _) = f(subject)
    ret

  def state: Any
  def outboundMessages: Export[DeviceId, String]
  def inboundMessages: Import[DeviceId, String]
  def previousState: Any
end ExchangeCalculusSemanticsImpl
