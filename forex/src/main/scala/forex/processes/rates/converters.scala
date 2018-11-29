package forex.processes.rates

import forex.services._

package object converters {
  import messages._

  def toProcessError[T <: Throwable](t: T): Error = t match {
    case OneForgeError.System(err) ⇒ Error.System(err)
    case OneForgeError.ConfigurationError(message) => Error.Generic(message)
    case OneForgeError.UpstreamAPIError(message) => Error.UpstreamError(message)
    case OneForgeError.CurrencyCodeError(message) => Error.BadRequest(message)
    case OneForgeError.RateNotAvailableError(message) => Error.BadRequest(message)
    case e: Error                  ⇒ Error.Generic(e.message)
    case e                         ⇒ Error.System(e)
  }

}
