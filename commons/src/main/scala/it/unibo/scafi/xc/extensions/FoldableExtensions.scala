package it.unibo.scafi.xc.extensions

import it.unibo.scafi.xc.abstractions.Foldable
import it.unibo.scafi.xc.abstractions.boundaries.{ LowerBounded, UpperBounded }

object FoldableExtensions:

  extension [T[_]: Foldable, N: Ordering: LowerBounded](foldable: T[N])
    def max: N = foldable.fold(summon[LowerBounded[N]].lowerBound)(implicitly[Ordering[N]].max)

  extension [T[_]: Foldable, N: Ordering: UpperBounded](foldable: T[N])
    def min: N = foldable.fold(summon[UpperBounded[N]].upperBound)(implicitly[Ordering[N]].min)

  private object NumericExtensions:
    import math.Numeric.Implicits.infixNumericOps

    extension [F[_]: Foldable, T](foldable: F[T])
      def count[N: Numeric]: N = foldable.fold(summon[Numeric[N]].zero)((acc, _) => acc + summon[Numeric[N]].one)
      def toList: List[T] = foldable.fold(List.empty[T])((acc, el) => el :: acc).reverse

    extension [T[_]: Foldable, N: Numeric](foldable: T[N])
      def sum: N = foldable.fold(Numeric[N].zero)(Numeric[N].plus)
      def product: N = foldable.fold(Numeric[N].one)(Numeric[N].times)
  export NumericExtensions._

  private object FractionalExtensions:
    import math.Fractional.Implicits.infixFractionalOps

    extension [T[_]: Foldable, N: Fractional](foldable: T[N])
      def average: N = foldable.sum / foldable.count

      def median: N = foldable.toList.sorted match
        case list if list.isEmpty => throw new IllegalArgumentException("Cannot calculate median of an empty list")
        case list if list.size % 2 == 0 =>
          (list(list.size / 2 - 1) + list(list.size / 2)) / summon[Numeric[N]].fromInt(2)
        case list => list(list.size / 2)

      def variance: N =
        val avg = foldable.average
        foldable.fold(Numeric[N].zero)((acc, el) => acc + (el - avg) * (el - avg)) / foldable.count
    export FractionalExtensions._
  end FractionalExtensions
end FoldableExtensions
