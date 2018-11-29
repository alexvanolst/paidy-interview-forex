package forex.services.oneforge

import cats.effect.IO
import forex.domain.Rate
import scalacache._
import scalacache.caffeine.CaffeineCache

import scala.concurrent.duration.Duration

trait OneForgeRatesCache {

  implicit val caffeineCache: Cache[Map[Rate.Pair, Rate]] = CaffeineCache[Map[Rate.Pair, Rate]]

  import scalacache.CatsEffect.modes.io

  def getCache: IO[Option[Map[Rate.Pair, Rate]]] = get("all")
  def setCache(values: Map[Rate.Pair, Rate], expiry: Duration): IO[Unit] = put("all")(values, Some(expiry)).map(_ ⇒ ())

}

object OneForgeRatesCache extends OneForgeRatesCache
