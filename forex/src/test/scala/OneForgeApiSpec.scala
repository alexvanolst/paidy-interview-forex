import java.time.Instant

import forex.config.{OneForgeApiConfig, OneForgeConfig, OneForgePriceConfig}
import forex.domain.Currency
import forex.services.oneforge.OneForgeApi
import forex.services.oneforge.Protocol._
import org.scalatest._
import io.circe.parser.decode

import scala.concurrent.duration._
class OneForgeApiSpec extends FlatSpec with Matchers with EitherValues {

  "1Forge Example Quote" should "be parsed correctly" in {

    val example = """{
                    |	"symbol": "AUDUSD",
                    |	"price": 0.792495,
                    |	"bid": 0.79248,
                    |	"ask": 0.79251,
                    |	"timestamp": 1502160793
                    |}
    """.stripMargin

    val f = decode[OFQuote](example)
    println(example)
    println(f.toString)

    decode[OFQuote](example).right.value should be(
      OFQuote(
        OFPair(Currency.AUD, Currency.USD),
        BigDecimal("0.792495"),
        BigDecimal("0.79248"),
        BigDecimal("0.79251"),
        Instant.ofEpochSecond(1502160793)
      )
    )
  }

  "All pairs url" should "be correct" in {
    val expected = "https://forex.1forge.com/1.0.3/quotes?pairs=AUDCAD,AUDCHF,AUDEUR,AUDGBP,AUDNZD,AUDJPY,AUDSGD,AUDUSD,CADAUD,CADCHF,CADEUR,CADGBP,CADNZD,CADJPY,CADSGD,CADUSD,CHFAUD,CHFCAD,CHFEUR,CHFGBP,CHFNZD,CHFJPY,CHFSGD,CHFUSD,EURAUD,EURCAD,EURCHF,EURGBP,EURNZD,EURJPY,EURSGD,EURUSD,GBPAUD,GBPCAD,GBPCHF,GBPEUR,GBPNZD,GBPJPY,GBPSGD,GBPUSD,NZDAUD,NZDCAD,NZDCHF,NZDEUR,NZDGBP,NZDJPY,NZDSGD,NZDUSD,JPYAUD,JPYCAD,JPYCHF,JPYEUR,JPYGBP,JPYNZD,JPYSGD,JPYUSD,SGDAUD,SGDCAD,SGDCHF,SGDEUR,SGDGBP,SGDNZD,SGDJPY,SGDUSD,USDAUD,USDCAD,USDCHF,USDEUR,USDGBP,USDNZD,USDJPY,USDSGD&api_key=secret"

    val ofConfig = OneForgeConfig(OneForgeApiConfig("https://forex.1forge.com/1.0.3", "secret"), OneForgePriceConfig(300.seconds))

    OneForgeApi.ratesUrl(ofConfig) should ===(expected)
  }

}
