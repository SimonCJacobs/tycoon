package jacobs.tycoon

import jacobs.tycoon.application.Application

suspend fun main() {
    Application().startAndWaitForConnections()
}