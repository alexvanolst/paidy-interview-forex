package forex.main

import cats.effect.IO
import forex.config._
import forex.services.OneForgeError
import org.atnos.eff._
import org.atnos.eff.all._
import org.zalando.grafter.macros._
import org.atnos.eff.addon.cats.effect.IOEffect._

import scala.concurrent.Future

@readerOf[ApplicationConfig]
case class Runners() {

  def runApp[R](
      app: AppEffect[R]
  ): Future[R] = {

    val f = runEitherCatchLeft[AppStack, Fx1[IO], OneForgeError, R](app) (
      error => fromIO(IO.raiseError(error))
    )

    unsafeToFuture(f)
  }
}
