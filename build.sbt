import Def.SettingsDefinition.wrapSettingsDefinition

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

// common settings
val ci = scala.sys.env.get("CI").contains("true")

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-new-syntax",
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
  ) ++ (if (ci)
          Seq(
            "-explain",
          )
        else Seq.empty),
)

// projects
lazy val commons = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "commons",
  )

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "core",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.17" % Test,
  )
  .dependsOn(commons)

lazy val simulator = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "simulator",
  )
  .dependsOn(core)

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
