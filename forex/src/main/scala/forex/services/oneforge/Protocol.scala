package forex.services.oneforge

import java.time.Instant

import forex.domain.Currency
import io.circe._
import io.circe.generic.semiauto._

object Protocol {

  case class OFPair(from: Currency, to: Currency)

  case class OFQuote(symbol: OFPair, price: BigDecimal, bid: BigDecimal, ask: BigDecimal, timestamp: Instant)

  implicit val instantMillisDecoder: Decoder[Instant] = Decoder[Long].map(Instant.ofEpochSecond)

  implicit val currencyDecoder: Decoder[Currency] = deriveDecoder[Currency]

  implicit val ofPairDecoder: Decoder[OFPair] = Decoder[String].emap { pair ⇒
    for {
      from ← Currency.fromString(pair.take(3))
      to ← Currency.fromString(pair.drop(3))
    } yield OFPair(from, to)
  }

  implicit val ofQuoteDecoder: Decoder[OFQuote] = deriveDecoder[OFQuote]

}
