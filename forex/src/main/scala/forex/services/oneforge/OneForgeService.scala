package forex.services.oneforge

import cats.effect._
import forex.config.OneForgeConfig
import forex.domain.{Price, Rate, Timestamp}
import forex.services.OneForgeError
import forex.services.oneforge.Error._
import forex.services.oneforge.Interpreters._errorEither
import forex.services.oneforge.Protocol._
import org.atnos.eff._
import org.atnos.eff.all._

import org.http4s.client.blaze._
import org.atnos.eff.addon.cats.effect.IOEffect._
import org.http4s.client.Client


class OneForgeService[R] private[oneforge] (
    implicit
    m1: _io[R],
    m2: _errorEither[R]
) extends Algebra[Eff[R, ?]] {

  override def get(pair: Rate.Pair): Eff[R, Error Either Rate] =
    for {
      client ← fromIO(Http1Client[IO]())
      config ← loadConfig
      allRates ← cachedOrLatestRates(client, config)
    } yield findRate(pair, allRates)

  def findRate(pair: Rate.Pair, allRates: Map[Rate.Pair, Rate]): Error Either Rate = {

    allRates.get(pair) match {
      case Some(result) => Right(result)
      case None => Left(RateNotAvailableError(pair.asString))
    }
  }

  def loadConfig: Eff[R, OneForgeConfig] = {
     fromEither[R, OneForgeError, OneForgeConfig](
       pureconfig.loadConfig[OneForgeConfig]("app.oneforge")
       .left
       .map( error => ConfigurationError(error.toList.mkString(", "))))
  }

  def cachedOrLatestRates(client: Client[IO], config: OneForgeConfig): Eff[R, Map[Rate.Pair, Rate]] = {

    val f = fromIO(OneForgeRatesCache.getCache)
     f flatMap {
      case Some(rates) =>
        Eff.pure(rates)
      case None =>
        for (
          latest <- getLatestRates(client, config);
          _ <- fromIO(OneForgeRatesCache.setCache(latest, config.price.maxPriceDelay))
        ) yield latest
      }
    }

  def getLatestRates(client: Client[IO], ofConfig: OneForgeConfig): Eff[R,Map[Rate.Pair,Rate]] = {

    val quotes: Eff[R,Seq[OFQuote]] =
      for (
        response <- fromIO(OneForgeApi.oneForgeRatesApi(client, ofConfig));
        parsed <- {
          fromEither[R, OneForgeError, Seq[OFQuote]](OneForgeApi.parseQuotes(response))
        }
      ) yield parsed

   quotes.map(_.map{
      q => {
        val pair = Rate.Pair(q.symbol.from, q.symbol.to)
        (pair, Rate(pair, Price(q.price), Timestamp.fromInstantUTC(q.timestamp)) )
    }}.toMap)
  }

}
