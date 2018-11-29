package forex.interfaces.api.rates

import akka.http.scaladsl._
import akka.http.scaladsl.unmarshalling.FromStringUnmarshaller
import akka.stream.Materializer
import forex.domain._
import forex.services.oneforge.Error.CurrencyCodeError

import scala.concurrent.{ ExecutionContext, Future }

trait Directives {
  import server.Directives._
  import unmarshalling.Unmarshaller
  import Protocol._

  def getApiRequest: server.Directive1[GetApiRequest] =
    for {
      from ← parameter('from.as(currency))
      to ← parameter('to.as(currency))
    } yield GetApiRequest(from, to)

  private val currency: Unmarshaller[String, Currency] = new FromStringUnmarshaller[Currency] {
    override def apply(value: String)(implicit ec: ExecutionContext, materializer: Materializer): Future[Currency] =
      Currency
        .fromString(value)
        .fold(
          err ⇒ Future.failed(CurrencyCodeError(err)),
          curr ⇒ Future.successful(curr)
        )
  }
}

object Directives extends Directives
