package forex.services.oneforge

import forex.domain._
import forex.services.OneForgeError
import org.atnos.eff._
import org.atnos.eff.addon.cats.effect.IOEffect._

object Interpreters {

  type ErrorEither[A] = OneForgeError Either A
  type _errorEither[R] = ErrorEither |= R

  def mock[R]: Algebra[Eff[R, ?]] = new Mock[R]

  def live[R](
      implicit
      m1: _io[R],
      m2: _errorEither[R]
  ): Algebra[Eff[R, ?]] = new OneForgeService[R]

}

final class Mock[R] private[oneforge] (
    )
    extends Algebra[Eff[R, ?]] {
  override def get(
      pair: Rate.Pair
  ): Eff[R, Error Either Rate] =
    Eff.pure[R, Error Either Rate](Right(Rate(pair, Price(BigDecimal(100)), Timestamp.now)))
}
