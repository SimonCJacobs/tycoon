package jacobs.deploy.tasks

import jacobs.deploy.aws.S3
import jacobs.deploy.configuration.ClientConfiguration
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

open class DeployClient : Deploy() {

    @InputDirectory
    val distributionsDirectory: Provider < DirectoryProperty > =
        project.providers.provider {
            ( this.project.convention.plugins[ "base" ] as BasePluginConvention ).distsDirectory
        }

    private val clientConfiguration: ClientConfiguration
        get() = deployConfiguration.get().clientConfiguration!!

    private val webpackTask: KotlinWebpack = project.tasks.withType < KotlinWebpack >()
        .findByName( "browserProductionWebpack" )!!

    init {
        @Suppress( "LeakingThis" )
        dependsOn( webpackTask )
    }

    override fun description(): String {
        return "Deploys the client code to AWS S3"
    }

    @TaskAction
    override fun deploy() {
        webpackTask.mode = KotlinWebpackConfig.Mode.PRODUCTION
        val s3 = S3 (
            bucket = deployConfiguration.get().domain,
            region = deployConfiguration.get().region,
            sdkCredentials = sdkCredentials()
        )
        s3.connect()
        s3.uploadFile( webpackTask.outputFile.toPath() )
        clientConfiguration.files.forEach { eachFilename ->
            s3.uploadFile( distributionsDirectory.get().file( eachFilename ).get().asFile.toPath() )
        }
    }

}