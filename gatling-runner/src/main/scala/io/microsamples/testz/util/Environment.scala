package io.microsamples.testz.util

object Environment {
  val chachkiesBaseUrl = scala.util.Properties.envOrElse("baseURL", "http://localhost:8080")
  val users = scala.util.Properties.envOrElse("numberOfUsers", "5")
  val maxResponseTime = scala.util.Properties.envOrElse("maxResponseTime", "10000") // in milliseconds
}
