package it.unibo.scafi.xc.language.syntax.common

case class RetSend[T](ret: T, send: T)

object RetSend:
  given [T]: Conversion[(T, T), RetSend[T]] = RetSend(_, _)
  given [T]: Conversion[T, RetSend[T]] = RetSend(_)
  given [F[_], T](using Conversion[T, F[T]]): Conversion[T, RetSend[F[T]]] = RetSend(_)
  def apply[T](retsend: T): RetSend[T] = RetSend(retsend, retsend)
