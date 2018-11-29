package forex.services.oneforge

sealed trait Error extends Throwable { //with NoStackTrace {
  val message: String
}

object Error {
  final case class RateNotAvailableError(rate: String) extends Error {
    val message = s"No price is available for $rate"
  }
  final case class CurrencyCodeError(message: String) extends Error
  final case class UpstreamAPIError(message: String) extends Error
  final case class ConfigurationError(message: String) extends Error
  final case class Generic(message: String) extends Error {}
  final case class System(underlying: Throwable) extends Error {
    val message = underlying.getMessage
  }
}
