import java.util.Properties

val globalSbtProperties = {
  val prop = new Properties()
  IO.load(prop, Path.userHome / ".sbt" / "config" / "global-sbt-config.properties")
  prop
}

val publishRepoUrl = globalSbtProperties.getProperty("publishRepoUrl")
val publishRepoUser = globalSbtProperties.getProperty("publishRepoUser")
val keyFile = Path.userHome / ".ssh" / "id_rsa"
val publishSshRepo = Resolver.ssh("publish repo", publishRepoUrl,22)(Resolver.ivyStylePatterns) as (publishRepoUser, keyFile) withPublishPermissions("0755")
//credentials += Credentials(Path.userHome / ".sbt" / "credentials" / ".publish-credentials")

lazy val liftVersion = settingKey[String]("Version number of the Lift Web Framework")
lazy val liftEdition = settingKey[String]("Lift Edition (short version number to append to artifact name)")

moduleName := "lift-jquery-module"
name := "lift-jquery-module"
organization := "net.liftmodules"
version := "2.12-LN"
liftVersion := { liftVersion ?? "3.4.3" }.value
liftEdition := { liftVersion apply { _.substring(0,3) } }.value
moduleName := { name.value + "_" + liftEdition.value }
scalaVersion := "2.13.6"
scalacOptions ++= Seq("-unchecked", "-deprecation")
crossScalaVersions := Seq(scalaVersion.value, "2.12.14")

logLevel := Level.Info  

resolvers ++= Seq(
  "Scala Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Scala" at "https://oss.sonatype.org/content/groups/scala-tools/"
)

ThisBuild / libraryDependencies ++= {
  "net.liftweb" %% "lift-webkit"  % liftVersion.value % "provided" ::
  "net.liftweb" %% "lift-testkit" % liftVersion.value % "provided" ::
  Nil
}

libraryDependencies ++= { 
  "ch.qos.logback" % "logback-classic" % "1.0.0" % "provided" ::
  "log4j" % "log4j" % "1.2.16" % "provided" ::
  Nil
}

ThisBuild / libraryDependencies ++= Seq(
   "org.specs2" %% "specs2-core" % "4.12.12" % "test",
   "org.specs2" %% "specs2-matcher-extra" % "4.12.12" % "test", //lift 3.1.x
   "org.specs2" %% "specs2-scalacheck" % "4.12.12" % "test" //lift 3.1.x
)

ThisBuild / publishTo := Some(publishSshRepo)

publishMavenStyle := true

Test / publishArtifact := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/karma4u101/lift-jquery-module</url>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://maven.apache.org/ref/2.1.0/maven-profile/license.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:karma4u101/lift-jquery-module.git</url>
    <connection>scm:git:git@github.com:karma4u101/lift-jquery-module.git</connection>
  </scm>
  <developers>
    <developer>
      <id>karma4u101</id>
      <name>Peter Petersson</name>
      <url>http://www.media4u101.se</url>
    </developer>
  </developers>
)






