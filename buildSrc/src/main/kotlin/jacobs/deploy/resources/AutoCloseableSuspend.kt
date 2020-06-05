package jacobs.deploy.resources

interface AutoCloseableSuspend {
    suspend fun close()
}