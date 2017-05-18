name := """MedicalClinicApp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "com.typesafe.play" %% "play-mailer" % "5.0.0",
  "it.innove" % "play2-pdf" % "1.5.1",
  cache,
  javaWs,
  "be.objectify" %% "deadbolt-java" % "2.5.4"
)
fork in run := true