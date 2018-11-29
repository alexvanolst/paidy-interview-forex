name := "forex"
version := "1.0.0"

scalaVersion := "2.12.4"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-Ypartial-unification",
  "-language:experimental.macros",
  "-language:implicitConversions",
  "-Xlint",
  "-Xfatal-warnings"
)

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

val http4sVersion = "0.18.0"
val scalaTestVersion = "3.0.5"
val circeVersion = "0.10.0"
val effVersion = "5.3.0"
val scalaCacheVersion = "0.24.0"

libraryDependencies ++= Seq(
  "com.github.pureconfig"      %% "pureconfig"             % "0.7.2",
  "com.softwaremill.quicklens" %% "quicklens"              % "1.4.11",
  "com.typesafe.akka"          %% "akka-actor"             % "2.4.19",
  "com.typesafe.akka"          %% "akka-http"              % "10.0.10",
  "de.heikoseeberger"          %% "akka-http-circe"        % "1.18.1",
  "io.circe"                   %% "circe-core"             % circeVersion,
  "io.circe"                   %% "circe-generic"          % circeVersion,
  "io.circe"                   %% "circe-java8"            % circeVersion,
  "io.circe"                   %% "circe-generic-extras"   % circeVersion,
  "io.circe"                   %% "circe-parser"           % circeVersion,
  "org.atnos"                  %% "eff"                    % effVersion,
  "org.atnos"                  %% "eff-monix"              % effVersion,
  "org.atnos"                  %% "eff-cats-effect"        % effVersion,
  "org.typelevel"              %% "cats-core"              % "1.1.0",
  "org.zalando"                %% "grafter"                % "2.6.0",
  "ch.qos.logback"             % "logback-classic"         % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging"          % "3.7.2",
  "com.github.cb372"           %% "scalacache-caffeine"    % scalaCacheVersion,
  "com.github.cb372"           %% "scalacache-cats-effect" % scalaCacheVersion,
  "org.http4s"                 %% "http4s-circe"           % http4sVersion,
  "org.http4s"                 %% "http4s-dsl"             % http4sVersion,
  "org.http4s"                 %% "http4s-blaze-client"    % http4sVersion,
  "org.scalactic"              %% "scalactic"              % scalaTestVersion % "test",
  "org.scalatest"              %% "scalatest"              % scalaTestVersion % "test",
  compilerPlugin("org.spire-math"  %% "kind-projector" % "0.9.4"),
  compilerPlugin("org.scalamacros" %% "paradise"       % "2.1.1" cross CrossVersion.full),
)
