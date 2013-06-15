import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._

object ApplicationBuild extends Build {

    val appName         = "login"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      javaCore,
      javaJdbc,
      javaEbean
    )

     val main = play.Project(appName, appVersion, appDependencies)
        // Add your own project settings here
        .settings(Play2WarPlugin.play2WarSettings: _*)
        .settings(Play2WarKeys.servletVersion := "3.0")

}