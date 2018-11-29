package forex.main

import forex.config._
import forex.processes.rates
import forex.{services => s}
import forex.{processes => p}
import org.zalando.grafter.macros._

@defaultReader[LiveProcesses]
trait RatesProcesses {
  implicit val _oneForge: s.OneForge[AppEffect]
  val Rates: rates.Processes[AppEffect]
}

@readerOf[ApplicationConfig]
case class LiveProcesses() extends RatesProcesses {

  implicit final lazy val _oneForge: s.OneForge[AppEffect] =
    s.OneForge.live[AppStack]

  final val Rates: rates.Processes[AppEffect] = p.Rates[AppEffect]

}

@readerOf[ApplicationConfig]
case class MockProcesses() extends RatesProcesses {

  implicit final lazy val _oneForge: s.OneForge[AppEffect] =
    s.OneForge.mock[AppStack]

  final val Rates: rates.Processes[AppEffect] = p.Rates[AppEffect]

}

