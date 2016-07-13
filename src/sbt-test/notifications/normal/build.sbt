lazy val root = (project in file(".")).enablePlugins(NotificationsPlugin)

name := "hello"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"