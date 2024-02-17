package it.unibo.scafi.xc.abstractions

/**
 * A type class that provides ways to combine collections of values into new collections of values.
 * @tparam F
 *   the type of the collection
 */
trait Liftable[F[_]] extends Mappable[F]:
  def lift[A, B](a: F[A])(f: A => B): F[B]

  def lift[A, B, C](a: F[A], b: F[B])(f: (A, B) => C): F[C]

  def lift[A, B, C, D](a: F[A], b: F[B], c: F[C])(f: (A, B, C) => D): F[D]

  extension [A](a: F[A]) override def map[B](f: A => B): F[B] = lift(a)(f)

object Liftable:

  def lift[A, B, F[_]: Liftable](a: F[A])(f: A => B): F[B] = summon[Liftable[F]].lift(a)(f)

  def liftTwice[A, B, F1[_]: Liftable, F2[_]: Liftable](a: F1[F2[A]])(f: A => B): F1[F2[B]] = lift(a)(aa => lift(aa)(f))

  def lift[A, B, C, F[_]: Liftable](a: F[A], b: F[B])(f: (A, B) => C): F[C] = summon[Liftable[F]].lift(a, b)(f)

  def liftTwice[A, B, C, F1[_]: Liftable, F2[_]: Liftable](a: F1[F2[A]], b: F1[F2[B]])(f: (A, B) => C): F1[F2[C]] =
    lift(a, b)((aa, bb) => lift(aa, bb)(f))

  def lift[A, B, C, D, F[_]: Liftable](a: F[A], b: F[B], c: F[C])(f: (A, B, C) => D): F[D] =
    summon[Liftable[F]].lift(a, b, c)(f)

  def liftTwice[A, B, C, D, F1[_]: Liftable, F2[_]: Liftable](a: F1[F2[A]], b: F1[F2[B]], c: F1[F2[C]])(
      f: (A, B, C) => D,
  ): F1[F2[D]] =
    lift(a, b, c)((aa, bb, cc) => lift(aa, bb, cc)(f))

  def lift[A, B, F[_]: Liftable](f: A => B): F[A] => F[B] = a => lift(a)(f)

  def lift[A, B, C, F[_]: Liftable](f: (A, B) => C): (F[A], F[B]) => F[C] = (a, b) => lift(a, b)(f)

  def lift[A, B, C, D, F[_]: Liftable](f: (A, B, C) => D): (F[A], F[B], F[C]) => F[D] = (a, b, c) => lift(a, b, c)(f)
end Liftable
