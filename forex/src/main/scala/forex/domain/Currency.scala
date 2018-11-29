package forex.domain

import cats.Show
import io.circe._

sealed trait Currency
object Currency {
  final case object AUD extends Currency
  final case object CAD extends Currency
  final case object CHF extends Currency
  final case object EUR extends Currency
  final case object GBP extends Currency
  final case object NZD extends Currency
  final case object JPY extends Currency
  final case object SGD extends Currency
  final case object USD extends Currency

  val all: Seq[Currency] = Seq(AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD)

  val pairs: Seq[Rate.Pair] = for {
    a ← all
    b ← all if b != a
  } yield Rate.Pair(a, b)

  implicit val show: Show[Currency] = Show.show {
    case AUD ⇒ "AUD"
    case CAD ⇒ "CAD"
    case CHF ⇒ "CHF"
    case EUR ⇒ "EUR"
    case GBP ⇒ "GBP"
    case NZD ⇒ "NZD"
    case JPY ⇒ "JPY"
    case SGD ⇒ "SGD"
    case USD ⇒ "USD"
  }

  def fromString(s: String): String Either Currency = s match {
    case "AUD" | "aud" ⇒ Right(AUD)
    case "CAD" | "cad" ⇒ Right(CAD)
    case "CHF" | "chf" ⇒ Right(CHF)
    case "EUR" | "eur" ⇒ Right(EUR)
    case "GBP" | "gbp" ⇒ Right(GBP)
    case "NZD" | "nzd" ⇒ Right(NZD)
    case "JPY" | "jpy" ⇒ Right(JPY)
    case "SGD" | "sgd" ⇒ Right(SGD)
    case "USD" | "usd" ⇒ Right(USD)
    case unmatched     ⇒ Left(s"Unknown currency code $unmatched")
  }

  implicit val encoder: Encoder[Currency] =
    Encoder.instance[Currency] { show.show _ andThen Json.fromString }

}
