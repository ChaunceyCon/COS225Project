Intructions for terminal:

Go into (this repo)/COS225Project and run:

    mvn compile

    mvn exec:java -Dexec.mainClass="com.storyreview.story.Startup"

First command compiles everything so package com.storyreview.whatever doesn't error anymore and DBHandler can import the MongoDB stuff.
Second command actually runs the main() method in com.storyreview.story.Startup. Before executing this command make sure it's not going to flood MongoDB with a bunch of duplicate entries (comment out the try-catch block in main() that creates and uses the DBHandler).

