package io.microsamples.loader

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import ru.tinkoff.gatling.amqp.Predef._
import ru.tinkoff.gatling.amqp.protocol.AmqpProtocolBuilder

import scala.concurrent.duration._

class PublishExample extends Simulation {

  val amqpConf: AmqpProtocolBuilder = amqp
    .connectionFactory(
      rabbitmq
        .host("localhost")
        .port(5672)
        .username("gatling")
        .password("gatling")
    )
    .usePersistentDeliveryMode

  val scn: ScenarioBuilder = scenario("AMQP test")
    .feed(Utils.idFeeder)
    .exec(
      amqp("publish to exchange").publish
        .queueExchange("soundbits.soundbitsGroup")
        .textMessage("Hello message - ${id}")
        .messageId("${id}")
        .priority(0)
        .property("testheader", "testvalue")
    )

  setUp(
    scn.inject(rampUsersPerSec(1) to 3 during (60 seconds), constantUsersPerSec(3) during (2 minutes))
  ).protocols(amqpConf)
    .maxDuration(2 minutes)

}
