ThisBuild / version := "0.1.0-SNAPSHOT"

// environment
val ci = scala.sys.env.get("CI").contains("true")

// scala
ThisBuild / scalaVersion := "3.3.1"

ThisBuild / scalacOptions ++= Seq(
  "-new-syntax",
  "-deprecation",
  "-indent",
  "-feature",
  "-Werror",
  "-Wunused:all",
  "-Wvalue-discard",
  "-Wnonunit-statement",
  "-Yexplicit-nulls",
  "-Ysafe-init",
  "-Ycheck-reentrant",
  "-language:strictEquality",
  "-language:implicitConversions",
) ++ (if (ci) Seq("-explain") else Nil)

// testing
ThisBuild / libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.17" % Test

// projects
lazy val commons = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    name := "commons",
  )

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    name := "core",
  )
  .dependsOn(commons)

lazy val simulator = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    name := "simulator",
  )
  .dependsOn(core)

lazy val tests = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    name := "tests",
  )
  .dependsOn(core, simulator)

// conventional commits
Global / onLoad ~= (_ andThen ("conventionalCommits" :: _))

// scalafix
ThisBuild / scalafixOnCompile := !ci
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

// scalafmt
ThisBuild / scalafmtOnCompile := !ci
ThisBuild / scalafmtPrintDiff := true

// sbt
Global / onChangedBuildSource := ReloadOnSourceChanges
Global / autoStartServer := !ci
Global / excludeLintKeys += autoStartServer
