Global / onChangedBuildSource := ReloadOnSourceChanges

Test / turbo := true

ThisBuild / autoStartServer := false
ThisBuild / includePluginResolvers := true
ThisBuild / turbo := true
ThisBuild / useSuperShell := false

ThisBuild / watchBeforeCommand := Watch.clearScreen
ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger

ThisBuild / organization := "$organization;format="lower,package"$"
ThisBuild / scalaVersion := "$scala_version$"
ThisBuild / version := "0.0.1-SNAPSHOT"

lazy val `$name;format="norm"$` =
  project
    .in(file("."))
    .settings(name := "$name$")
    .settings(commonSettings: _*)
    .settings(commonDependencies: _*)
    .aggregate($module;format="lower,word"$, example)

lazy val $module$ =
  project
    .in(file("$module$"))
    .settings(name := "$module$")
    .settings(commonSettings: _*)
    .settings(commonDependencies: _*)
    .settings(
      scalacOptions ++= extendedCompilerOptions,
      libraryDependencies ++= Seq(
        `specs2-core`       % Test,
        `specs2-scalacheck` % Test
      )
    )

lazy val example =
  project
    .in(file("example"))
    .settings(name := "example")
    .settings(commonSettings: _*)
    .settings(commonDependencies: _*)
    .settings(
      libraryDependencies ++= Seq(
        `ammonite` % Test
      ),
      sourceGenerators in Test += Def.task {
        val file = (sourceManaged in Test).value / "amm.scala"
        IO.write(file, s"object amm extends App { ammonite.Main(\$ammInitialCommands).run() }")
        Seq(file)
      }.taskValue // test:run
    )
    .dependsOn($module$)

lazy val ammInitialCommands =
  "\"import example._\""

lazy val commonSettings = Seq(
  addCompilerPlugin(`better-monadic-for`),
  addCompilerPlugin(`context-applied`),
  addCompilerPlugin(`kind-projector`),
  update / evictionWarningOptions := EvictionWarningOptions.empty,
  scalacOptions ++= baseCompilerOptions,
)

lazy val commonDependencies = Seq()

lazy val `ammonite`           = "com.lihaoyi"    %% "ammonite"           % "2.2.0" cross CrossVersion.full
lazy val `better-monadic-for` = "com.olegpy"     %% "better-monadic-for" % "0.3.1"
lazy val `context-applied`    = "org.augustjune" %% "context-applied"    % "0.1.4"
lazy val `kind-projector`     = "org.typelevel"  %% "kind-projector"     % "0.11.0" cross CrossVersion.full
lazy val `specs2-core`        = "org.specs2"     %% "specs2-core"        % "4.10.3"
lazy val `specs2-scalacheck`  = "org.specs2"     %% "specs2-scalacheck"  % "4.10.3"

lazy val baseCompilerOptions = Seq(
  "-encoding",
  "utf-8",                  // Specify character encoding used by source files.
  "-deprecation",           // Emit warning and location for usages of deprecated APIs.
  "-explaintypes",          // Explain type errors in more detail.
  "-feature",               // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials", // Existential types (besides wildcard types) can be written and inferred.
  "-language:higherKinds",  // Allow higher-kinded types.
  "-unchecked",             // Enable additional warnings where generated code depends on assumptions.
)

lazy val extendedCompilerOptions = Seq(
  "-Xcheckinit",            // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfatal-warnings",       // Fail the compilation if there are any warnings.
  "-Ywarn-dead-code",       // Warn when dead code is identified.
  "-Ywarn-extra-implicit",  // Warn when more than one implicit parameter section is defined.
  "-Ywarn-unused"           // Enable or disable specific unused warning.
)
