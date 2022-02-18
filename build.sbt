import org.scalajs.linker.interface.{ModuleInitializer, ModuleSplitStyle, OutputPatterns}
import locales._

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := """finalibre-saxo""",
    scalaJSProjects := Seq(client),
    Assets / pipelineStages := Seq(scalaJSPipeline),
    Compile / compile := (( Compile / compile ) dependsOn scalaJSPipeline).value,
    localesFilter := LocalesFilter.Selection("da", "da-DK"),
    libraryDependencies ++= Seq(
      ws,
      guice,
      "org.webjars" % "bootstrap" % "5.1.3",
      "org.webjars" % "jquery" % "3.6.0",
      "org.webjars" % "requirejs" % "2.3.6",
      "com.typesafe.slick" %% "slick" % "3.3.3",
      "com.zaxxer" % "HikariCP" % "2.4.2",
      "org.postgresql" % "postgresql" % "42.2.24",
      "org.jsoup" % "jsoup" % "1.13.1",
      "com.google.api-client" % "google-api-client" % "1.32.1",
      "com.auth0" % "java-jwt" % "3.18.2"
    ),
    Compile / resourceGenerators += Def.task {
      //val clientTarget = ( fastLinkJS in Compile in client).value.data
      val bundles = (client / Compile / fastOptJS / webpack ).value.map(_.data)
      bundles.foreach(bund => println(s"Generated bundle: ${bund.getAbsolutePath} during webpack"))
      bundles
    }.taskValue,
    PlayKeys.devSettings ++= IO.readLines(new File("./conf/devrunsettings.conf")).map(_.split("=")).map(p => p(0) -> p(1))
  )
  .enablePlugins(PlayScala, LauncherJarPlugin, LocalesPlugin)
  .dependsOn(client)



lazy val client = (project in file("client"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.0.0",
      "org.querki" %%% "querki-jsext" % "0.10",
      //"io.github.cquiroz" %%% "locales-minimal-en-db" % "1.1.1",
      "io.github.cquiroz" %%% "scala-java-time" % "2.2.0" ,
      "io.github.cquiroz" %%% "scala-java-locales" % "1.2.0"
    ) ,
    Compile / npmDependencies  ++= Seq(
      "webpack-merge" -> "5.8.0",
     // "style-loader" -> "3.3.1",
      //"@popperjs/core"-> "2.0.0",
      "popper.js" -> "1.16.1",
      "jquery" -> "3.6.0",
      "bootstrap" -> "5.1.3",
      "@types/selectize" -> "0.12.34",
      "selectize" -> "0.12.6",
      "@editorjs/editorjs" -> "2.19.3",
      "@editorjs/header" -> "2.6.1",
      "@editorjs/image" -> "2.6.0",
      "@editorjs/list" -> "1.6.2",
      "@editorjs/embed" -> "2.4.0"/*,
      "@types/jquery" -> "3.5.13"*/
    ),
    stIgnore := List(
      "sass-loader",
      "jquery",
      "bootstrap",
      "webpack",
      "webpack-merge",
      "style-loader",
      "@editorjs/header",
      "@editorjs/image",
      "@editorjs/list",
      "@editorjs/embed"
    ),
    Compile / sourceGenerators   += Def.task {
      val _ = (Compile / npmInstallDependencies).value
      Seq.empty[File]
    },
    unmanagedBase := baseDirectory.value / "lib",
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    webpackConfigFile := Some(baseDirectory.value / "../conf/finalibre-saxo.webpack.config.js"),
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule)}
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb, ScalaJSBundlerPlugin, ScalablyTypedConverterPlugin)


val circeVersion = "0.14.1"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.5",
  organization := "finalibre",
  version := "1.0-SNAPSHOT",
  libraryDependencies ++= Seq(
    "commons-io" % "commons-io" % "2.8.0",
    "io.circe" %%% "circe-core" % circeVersion,
    "io.circe" %%% "circe-generic" % circeVersion,
    "io.circe" %%% "circe-parser" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion
  )
)


