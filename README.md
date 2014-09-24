events-fun
==========

A demo of high feedback app in Java. The project includes a calendar for laying out events in a calendar.

Development notes
=================

Prerequisites:
--------------

* JDK 8
* Eclipse (or IntelliJ)
* Maven
* PostgreSQL
* Git
* Heroku toolbelt (for deployment)

Getting started:
----------------

These steps takes you through importing the project in your workspace, creating the necessary databases, executing tests, running the project in the debugger, building a deployment package and deploying the project to Heroku.

1. `git clone https://github.com/jhannes/events-fun.git`
1. `mvn eclipse:eclipse -DdownloadSources` (or similar with IntelliJ)
1. In Eclipse: File -> Import -> Existing projects into workspace... (or similar with IntelliJ)
1. Create a PostgreSQL test database with settings like com.johannesbrodwall.infrastructure.db.TestDatabase
1. Run the tests as JUnit tests
1. Register your application on Google Developer Console https://console.developers.google.com/project. The callback URL should be http://localhost:10080/events/oauth2callback
1. Put the Client ID and Client Secret from Google as `oauth2.google.clientId` and `oauth2.google.clientSecret` in an `event.properties` file in the project root
1. Create a PostgreSQL development database and put the credentals in event.properties
1. Run `com.johannesbrodwall.events.EventsApplicationServer` as a main class. It's useful to run this in the debugger
1. Access the application on http://localhost:10080/ (NB: ip-address in URL will fail to authorize with Google)
1. `mvn package` builds an executable jar file
1. Run the executable jar file with `java -jar target/events-fun-1.0.0.jar`. This looks for an `events.properties` in the current working directory
1. Create a application on http://heroku.com with a PostgreSQL database
1. Update the Google application to include the Heroku URL
1. `heroku config:set oauth2_google_clientId=<your client id>` from Google and `heroku config:set oauth2_google_clientSecret=<your client secret>`
1. `git push heroku`
