name := "amqp-stresser"

version := "0.1"

scalaVersion := "2.12.10"

resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"
libraryDependencies += "ru.tinkoff" %% "gatling-amqp-plugin" % "0.0.2"

