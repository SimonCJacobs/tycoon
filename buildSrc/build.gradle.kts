repositories {
    jcenter()
}

plugins {
     // TODO: Get this access to work in Intellij (it's ok on command line. Somehow the IDE runs the build
     // of this first and before evaluating gradle.properties
    kotlin( "jvm" ) version "1.3.72" // System.getProperty( "kotlin-version" )
}