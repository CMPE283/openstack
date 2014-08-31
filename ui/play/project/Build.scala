import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "sample"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    "com.google.code.gson" % "gson" % "2.2",
	"org.apache.jclouds.driver" % "jclouds-slf4j" % "1.8.0" ,
	"org.apache.jclouds.driver" % "jclouds-sshj" % "1.8.0" ,
	"org.apache.jclouds.api" % "openstack-keystone" % "1.8.0" ,
	"org.apache.jclouds.api" % "openstack-nova" % "1.8.0" ,
	"org.apache.jclouds.labs" % "openstack-swift" % "1.8.0" ,
	"org.apache.jclouds.api" % "openstack-cinder" % "1.8.0" ,
	"org.apache.jclouds.api" % "openstack-trove" % "1.8.0" ,
	"org.apache.jclouds.labs" % "openstack-glance" % "1.8.0" ,
	"org.apache.jclouds.labs" % "openstack-marconi" % "1.8.0" ,
	"org.apache.jclouds.labs" % "openstack-neutron" % "1.8.0" ,
	"ch.qos.logback" % "logback-classic" % "1.0.13" ,
	"mysql" % "mysql-connector-java" % "5.1.25"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
