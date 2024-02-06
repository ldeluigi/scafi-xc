package it.unibo.scafi.xc.engine.exchange

import scala.collection.{IndexedSeqView, MapView, mutable}

import it.unibo.scafi.xc.engine.network.{ Export, Import }
import it.unibo.scafi.xc.engine.path.*
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics

trait ExchangeCalculusSemanticsImpl:
  this: ExchangeCalculusSemantics with NValuesSemanticsImpl =>
  case class InvocationCoordinate(key: String, index: Int)
  type AAAA = String

  @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
  private var lastPopped = InvocationCoordinate("", 0)
  private val stack: mutable.Stack[InvocationCoordinate] = mutable.Stack.empty[InvocationCoordinate]

  private val sendMessages: mutable.Map[Path[InvocationCoordinate], Map.WithDefault[DeviceId, AAAA]] =
    mutable.Map.empty[Path[InvocationCoordinate], Map.WithDefault[DeviceId, AAAA]]
  private def currentPath: IndexedSeqView[InvocationCoordinate] = stack.view.reverse

  override def aligned: Set[DeviceId] = inboundMessages.view
    .filter(_._2.keys.exists(_.startsWith(currentPath)))
    .keys
    .toSet

  private def alignedMessages: Map[DeviceId, AAAA] =
    val _ = inboundMessages.view
    ???
//    .flatMap(_._2.get(currentPath.toList))
//    .toMap
  private def conversion[T](a: AAAA): T = ???

  private def conversion[T](a: T): AAAA = ???

  private def scope[T](key: String)(body: () => T): T =
    val next = if lastPopped.key == key then lastPopped.index + 1 else 0
    stack.push(InvocationCoordinate(key, next))
    val result = body()
    lastPopped = stack.pop()
    result

  override protected def br[T](cond: Boolean)(th: => T)(el: => T): T = scope(s"branch/$cond"): () =>
    if cond then th else el

  override protected def xc[T](init: AggregateValue[T])(
      f: AggregateValue[T] => (AggregateValue[T], AggregateValue[T]),
  ): AggregateValue[T] = scope("exchange"): () =>
    val messages = alignedMessages.map((k, v) => (k, conversion[T](v)))
    val subject = NValuesImpl[T](init.onlySelf, messages)
    val (ret, send) = f(subject)
    sendMessages += currentPath.toList -> Map.WithDefault(
      send.alignedValues.mapValues(conversion).toMap,
      _ => conversion(send.default),
    )
    ret

  def outboundMessages: Export[DeviceId, InvocationCoordinate, String] = sendMessages.toMap

  def inboundMessages: Import[DeviceId, InvocationCoordinate, String]
end ExchangeCalculusSemanticsImpl
