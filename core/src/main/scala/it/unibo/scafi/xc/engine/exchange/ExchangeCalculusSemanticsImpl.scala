package it.unibo.scafi.xc.engine.exchange

import scala.collection.{ mutable, MapView }
import it.unibo.scafi.xc.engine.network.{ Export, Import }
import it.unibo.scafi.xc.engine.path.MutableValueTree.*
import it.unibo.scafi.xc.engine.path.{ MutableValueTree, ValueTree, ValueTreeByMap }
import it.unibo.scafi.xc.engine.stack.mutable.IncrementalTreeStack
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait ExchangeCalculusSemanticsImpl:
  this: ExchangeCalculusSemantics with NValuesSemanticsImpl =>

  private val stack = IncrementalTreeStack[String, AggregateValue[Any]]()
  private val sendMessages: mutable.Map[Path[(String, Int)], Map.WithDefault[DeviceId, Any]] = mutable.Map.empty

  override def aligned: Set[DeviceId] = inboundMessages.view.filter(_._2.hasPrefix(stack.currentPath)).keys.toSet

  private def alignedMessages[T]: MapView[DeviceId, T] =
    inboundMessages.view.filterKeys(aligned).mapValues(v => as[T](v(stack.currentPath)))

  def previousValue[T](or: => T): T = ??? // as[T](previousState.getOrElse(stack.current, or))

  private def as[T](x: Any): T = x match
    case x: T @unchecked => x
    case _ => throw new IllegalStateException(s"Previous value is not of type ${x.getClass}")

  override protected def br[T](cond: Boolean)(th: => T)(el: => T): T = stack.scope(s"branch/$cond"): () =>
    if cond then (th, None) else (el, None)

  override protected def xc[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T] = stack.scope("exchange"): () =>
    val messages = alignedMessages[T]
    val previous = previousValue[T](or = init.onlySelf)
    val subject = NValuesImpl[T](previous, messages.toMap)
    val (ret, send) = f(subject)
    sendMessages += stack.currentPath -> send.alignedMap
    (ret, ???)

  def state: ValueTree[String, Any] = ???
  def outboundMessages: Export[DeviceId, (String, Int)] = ValueTreeByMap(sendMessages)

  def inboundMessages: Import[DeviceId, (String, Int)]
  def previousState: ValueTree[String, Any]
end ExchangeCalculusSemanticsImpl
