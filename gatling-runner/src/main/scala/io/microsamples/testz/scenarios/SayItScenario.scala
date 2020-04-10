package io.microsamples.testz.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object SayItScenario {

  val sayItPath = http("Say It Verify.")
    .get("/sayit")
    .check(status is 200)

  val sayItRoot = scenario("Process Publishe Message.")
    .exec(sayItPath)
}
