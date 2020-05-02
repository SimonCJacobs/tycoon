package jacobs.tycoon

import jacobs.tycoon.application.ApplicationBootstrapper

suspend fun main() {
    ApplicationBootstrapper().start()
}