ThisBuild / version := "0.1.0-SNAPSHOT"

// environment
val ci = scala.sys.env.get("CI").contains("true")

// scala
ThisBuild / scalaVersion := "3.4.0"

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
  "-Xcheck-macros",
  "-language:strictEquality",
  "-language:implicitConversions",
) ++ (if (ci) Seq("-explain") else Nil)

// testing
ThisBuild / libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.18" % Test

lazy val commonTestSettings = Seq(
  Test / scalacOptions --= Seq(
    "-Werror",
    "-Wunused:all",
    "-Wvalue-discard",
    "-Wnonunit-statement",
    "-Xcheck-macros",
  ),
)

// projects
lazy val commons = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    name := "commons",
    commonTestSettings,
  )

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    name := "core",
    commonTestSettings,
  )
  .dependsOn(commons % "compile->compile;test->test")

lazy val simulator = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    name := "simulator",
    commonTestSettings,
  )
  .dependsOn(core % "compile->compile;test->test")

lazy val tests = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    name := "tests",
    commonTestSettings,
  )
  .dependsOn(core % "test->test", simulator % "test->test")

lazy val `alchemist-incarnation` = project
  .settings(
    name := "alchemist-incarnation",
    libraryDependencies ++= Seq(
      "it.unibo.alchemist" % "alchemist" % "30.1.11",
      "it.unibo.alchemist" % "alchemist-api" % "30.1.11",
      "it.unibo.alchemist" % "alchemist-test" % "30.1.11",
    ),
    commonTestSettings,
  )
  .dependsOn(core.jvm)

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
