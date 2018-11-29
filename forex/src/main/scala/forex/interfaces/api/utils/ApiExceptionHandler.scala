package forex.interfaces.api.utils

import akka.http.scaladsl._
import akka.http.scaladsl.model.StatusCodes
import forex.processes._
import forex.processes.rates.messages.Error.{BadRequest, Generic, System, UpstreamError}

object ApiExceptionHandler {

  def apply(): server.ExceptionHandler =
    server.ExceptionHandler {
      case re: RatesError ⇒
        ctx ⇒
          re match {
            case UpstreamError(message) => ctx.complete((StatusCodes.BadGateway, s"Unable to complete request to due an error with the upstream service: $message"))
            case BadRequest(message) => ctx.complete((StatusCodes.BadRequest, s"Bad Request: $message"))
            case Generic(message) => ctx.complete((StatusCodes.InternalServerError, message))
            case System(underlying) =>  ctx.complete((StatusCodes.InternalServerError, underlying.getMessage))
          }
          ctx.complete("Something went wrong in the rates process")
      case t: Throwable ⇒
        ctx ⇒
          t.printStackTrace()
          ctx.complete("Something else went wrong")
    }

}
