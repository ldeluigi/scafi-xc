import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-Ywarn-unused",
    "-Ywarn-unused-import",
    "-Yexplicit-nulls",
    "-Ycheck-reentrant"
  )
)

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    name := "core"
  )

lazy val simulator = (project in file("simulator"))
  .settings(
    commonSettings,
    name := "simulator"
  )
  .dependsOn(core)

Global / onChangedBuildSource := ReloadOnSourceChanges
