name := "gojira"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(

  //Functional programming
  "org.typelevel" %% "cats-core" % "1.0.0-MF",

  //Testing
  "org.scalatest" %% "scalatest" % "3.0.1" % Test,

  //Akka (and testing)
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,

  //Transient dependencies from akka http (here for educational purposes)

  //  "com.typesafe.akka" %% "akka-actor" % "2.4.19",
  //  "com.typesafe.akka" % "akka-stream" % "2.4.19",

  //Logging
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.20",

  //Database
  "org.postgresql" % "postgresql" % "42.1.4",
  "org.flywaydb" % "flyway-core" % "4.2.0",
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "com.zaxxer" % "HikariCP" % "2.7.0",

  //Json library (encode/decode)
  "io.circe" %% "circe-core" % "0.8.0",
  "io.circe" %% "circe-generic" % "0.8.0",
  "io.circe" %% "circe-parser" % "0.8.0",
  "de.heikoseeberger" %% "akka-http-circe" % "1.18.0"
)


        