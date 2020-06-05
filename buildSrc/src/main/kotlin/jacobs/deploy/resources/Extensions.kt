package jacobs.deploy.resources

suspend fun < T : AutoCloseableSuspend, R > T.use( block: suspend T.() -> R ): R {
    return try {
        block()
    }
    finally {
        this.close()
    }
}