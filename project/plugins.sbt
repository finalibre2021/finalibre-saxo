addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.11")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.8.0")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.2.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"  % "1.1.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.20.0")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.5.1")
addSbtPlugin("io.github.cquiroz" % "sbt-locales" % "3.1.0")

resolvers += Resolver.bintrayRepo("oyvindberg", "converter")
addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta37")

dependencyOverrides += "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.0"
dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "1.3.0"
