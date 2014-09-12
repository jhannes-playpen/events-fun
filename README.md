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

# `git clone https://github.com/jhannes/events-fun.git`
# `mvn eclipse:eclipse -DdownloadSources` (or similar with IntelliJ)
# In Eclipse: File -> Import -> Existing projects into workspace... (or similar with IntelliJ)
# Create a PostgreSQL test database with settings like com.johannesbrodwall.infrastructure.db.TestDatabase
# Run the tests as JUnit tests
# Register your application on Google Developer Console https://console.developers.google.com/project. The callback URL should be http://localhost:10080/events/oauth2callback
# Put the Client ID and Client Secret from Google as `oauth2.google.clientId` and `oauth2.google.clientSecret` in an `event.properties` file in the project root
# Create a PostgreSQL development database and put the credentals in event.properties
# Run `com.johannesbrodwall.events.EventsApplicationServer` as a main class. It's useful to run this in the debugger
# Access the application on http://localhost:10080/ (NB: ip-address in URL will fail to authorize with Google)
# `mvn package` builds an executable jar file
# Run the executable jar file with `java -jar target/events-fun-1.0.0.jar`. This looks for an `events.properties` in the current working directory
# Create a application on http://heroku.com with a PostgreSQL database
# Update the Google application to include the Heroku URL
# `heroku config:set oauth2_google_clientId=<your client id>` from Google and `heroku config:set oauth2_google_clientSecret=<your client secret>`
# `git push heroku`
