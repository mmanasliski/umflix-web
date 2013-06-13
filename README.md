Webapp module for UMFlix

Web application for UMFlix. Includes website with some basic features as login, creating account, changing password, search and watch movies.

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
 2) Please download and "mvn install" the following repositories from which umflix depend:
        https://github.com/haretche2/clipstorage
        https://github.com/marshhxx/modelstorage
        https://github.com/scarrera/movie-manager
        https://github.com/martinbomio/usermanager
        https://github.com/haretche2/autenticationhandler
        https://github.com/mmanasliski/catalog-service
 3) Install playFramework http://www.playframework.com/download
    cd directory of umflix-web
    mvn play2:compile
    mvn play2:copy-dependencies
    mvn play2:run
 4) If the movie is not shown and ur not using Mozilla Firefox, install(or open with) Mozilla Firefox browser in order to see movies.