Webapp module for UMFlix
==========


How to run Webapp module.

1) Import web-app dependency

    In command prompt : git pull https://github.com/mmanasliski/umflix-web.git to wanted directory
                        cd wanted directory
                        mvn clean install
                        cd current project
                        add dependency to pom.xml
                             <dependency>
                                 <groupId>umflix-web</groupId>
                                 <artifactId>umflix-web</artifactId>
                                 <version>1.0-SNAPSHOT</version>
                             </dependency>
                        mvn install current project
 2) Install playFramework http://www.playframework.com/download
    cd directory of umflix-web
    play run
