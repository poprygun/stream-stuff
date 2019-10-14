package io.microsamples.loader

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object LoadRunner {

  def main(args: Array[String]): Unit = {

    val simulationClass = classOf[PublishExample].getName

    val props = new GatlingPropertiesBuilder
    props.binariesDirectory("./target/scala-2.12/classes")
    props.simulationClass(simulationClass)
    props.runDescription("My stupid describption")
    props.resultsDirectory(".")

    Gatling.fromMap(props.build)
  }

}
