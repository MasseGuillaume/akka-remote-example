val baseSettings = Seq(
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    akka("remote")
  )
)

def akka(module: String) = "com.typesafe.akka" %% ("akka-" + module) % "2.3.11"

lazy val sbtRunner = project
  .in(file("sbt-runner"))
  .settings(baseSettings)
  .settings(
    dockerfile in docker := new Dockerfile {
      from("openjdk:8u111-jdk-alpine")

      val artifact = assembly.value
      val artifactTargetPath = s"/app/${artifact.name}"

      add(artifact, artifactTargetPath)

      expose(5150)
      entryPoint("java", "-Xmx2G", "-Xms512M", "-jar", artifactTargetPath)
    }
  )
  .enablePlugins(DockerPlugin)

lazy val server = project
  .in(file("server"))
  .settings(baseSettings)
