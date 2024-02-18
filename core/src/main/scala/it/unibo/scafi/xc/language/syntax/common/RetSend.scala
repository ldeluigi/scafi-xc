package it.unibo.scafi.xc.language.syntax.common

/**
 * Syntax sugar to return and send a value at the same time.
 * @param ret
 *   the value to return
 * @param send
 *   the value to send
 * @tparam T
 *   the type of the values
 */
case class RetSend[+T](ret: T, send: T)

object RetSend:
  given [T]: Conversion[(T, T), RetSend[T]] = RetSend(_, _)
  given [T]: Conversion[T, RetSend[T]] = RetSend(_)
  given [F[_], T](using Conversion[T, F[T]]): Conversion[T, RetSend[F[T]]] = RetSend(_)
  given [F[_], T](using Conversion[T, F[T]]): Conversion[RetSend[T], RetSend[F[T]]] = rs => RetSend(rs.ret, rs.send)

  /**
   * Syntax sugar to return and send a single value.
   * @param retsend
   *   the value to return and send
   * @tparam T
   *   the type of the value
   * @return
   *   a RetSend instance with the same value for ret and send
   */
  def apply[T](retsend: T): RetSend[T] = RetSend(retsend, retsend)

  case class Continuation[+T](ret: T):
    inline def send[T2 >: T](send: T2): RetSend[T2] = RetSend(ret, send)

  inline def ret[T](ret: T): Continuation[T] = Continuation(ret)
end RetSend
