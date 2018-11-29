package forex.services.oneforge

import cats.effect.IO
import forex.config.OneForgeConfig
import forex.domain.{Currency, Rate}
import forex.services.OneForgeError
import forex.services.oneforge.Error.UpstreamAPIError
import forex.services.oneforge.Protocol.OFQuote
import io.circe.Json
import org.http4s.client.Client
import org.http4s.circe._

object OneForgeApi {

  def pairsParameter(pairs: Seq[Rate.Pair]): String =
    pairs.map(_.asString).mkString(",")

  def ratesUrl(oneForgeConfig: OneForgeConfig): String = {
    val pairsParameter = Currency.pairs.map(_.asString).mkString(",")
    val root = oneForgeConfig.api.root
    val key = oneForgeConfig.api.key

    s"$root/quotes?pairs=$pairsParameter&api_key=$key"
  }

  def oneForgeRatesApi(client: Client[IO],
                       oneForgeConfig: OneForgeConfig): IO[Json] = {
    client.expect(ratesUrl(oneForgeConfig))
  }

  def parseQuotes(response: Json): Either[OneForgeError, Seq[OFQuote]] = response.as[Seq[OFQuote]].left
    .map(decodeError ⇒ UpstreamAPIError(decodeError.message))

}
