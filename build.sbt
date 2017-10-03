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
  "ch.qos.logback"    % "logback-classic" % "1.2.3",

  "com.typesafe.akka" %% "akka-slf4j" % "2.4.20",

  //Slick & H2
  "com.h2database"      % "h2"              % "1.4.185",
  "com.typesafe.slick" %% "slick"           % "3.2.1",

  //Json library (encode/decode)
  "io.spray" %% "spray-json" % "1.3.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10"

)


        