package io.microsamples.testz.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.microsamples.testz.scenarios.SayItScenario
import io.microsamples.testz.util.{Environment, Headers}

import scala.concurrent.duration._

class SayItSimulation extends Simulation {
  val httpConf = http.baseUrl(Environment.chachkiesBaseUrl)
    .headers(Headers.commonHeader)

  val chachkiesScenarios = List(

    SayItScenario.sayItRoot.inject(
      rampUsersPerSec(1) to 100 during (10 seconds)
      , constantUsersPerSec(5) during (5 seconds)
//      , constantUsersPerSec(50) during (20 seconds)
    )
  )

  setUp(chachkiesScenarios)
    .protocols(httpConf)
    .maxDuration(5 minutes)

    .assertions(
      global.responseTime.max.lt(Environment.maxResponseTime.toInt)
    )

}
