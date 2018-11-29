package forex.processes.rates

import forex.domain._

package messages {
  sealed trait Error extends Throwable { //with NoStackTrace {
    val message: String

    // akka-http uses this to fill in error responses so we must make sure this returns a sensible result
    override def getMessage: String = message
  }

  object Error {
    final case class UpstreamError(message: String) extends Error
    final case class BadRequest(message: String) extends Error
    final case class Generic(message: String) extends Error
    final case class System(underlying: Throwable) extends Error {
      override val message: String = underlying.getMessage
    }
  }

  final case class GetRequest(
      from: Currency,
      to: Currency
  )
}
