package it.unibo.scafi.xc.language.syntax.common

case class RetSend[T](ret: T, send: T)

object RetSend:
  given [T]: Conversion[(T, T), RetSend[T]] = RetSend(_, _)
  given [T]: Conversion[T, RetSend[T]] = x => RetSend(x, x)
  given [F[_], T](using Conversion[T, F[T]]): Conversion[T, RetSend[F[T]]] = x => RetSend(x, x)
  def apply[T](ret: T, send: T): RetSend[T] = new RetSend(ret, send)
