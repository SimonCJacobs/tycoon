plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

kotlinDslPluginOptions {
    experimentalWarning.set( false )
}

dependencies {
    implementation( "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72" )
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.7" )
    implementation( "software.amazon.awssdk:ec2:2.13.22" )
    implementation( "software.amazon.awssdk:route53:2.13.22" )
    implementation( "software.amazon.awssdk:s3:2.13.22" )
    implementation( "com.jcraft:jsch:0.1.55" )
    implementation( "com.beust:klaxon:5.0.1" )
}
