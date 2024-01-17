ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

// common settings
lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-no-indent",
    "-Werror",
    "-Wunused:all",
    "-Wvalue-discard",
    "-Wnonunit-statement",
    "-Yexplicit-nulls",
    "-Ycheck-reentrant",
    "-language:strictEquality",
  ),
)

nativeConfig ~= { c =>
  if (scala.util.Properties.isWin) {
    c.withCompileOptions(c.compileOptions ++ Seq("-D_CRT_SECURE_NO_WARNINGS"))
  } else c
}

// projects
lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "core",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.17" % Test,
  )

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
ThisBuild / scalafixOnCompile := true
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

// sbt
Global / onChangedBuildSource := ReloadOnSourceChanges
