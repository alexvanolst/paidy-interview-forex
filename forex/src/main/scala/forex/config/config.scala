package forex.config

import org.zalando.grafter.macros._

import scala.concurrent.duration.FiniteDuration

@readers
case class ApplicationConfig(
    akka: AkkaConfig,
    api: ApiConfig,
    executors: ExecutorsConfig,
    oneforge: OneForgeConfig
)

case class AkkaConfig(
    name: String,
    exitJvmTimeout: Option[FiniteDuration]
)

case class ApiConfig(
    interface: String,
    port: Int
)

case class ExecutorsConfig(
    default: String
)

case class OneForgeConfig(api: OneForgeApiConfig, price: OneForgePriceConfig)

case class OneForgeApiConfig(root: String, key: String)

case class OneForgePriceConfig(maxPriceDelay: FiniteDuration)