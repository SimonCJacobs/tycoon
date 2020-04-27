package jacobs.websockets.engine

internal interface TimestampFactory {
    /**
     * Should return the number of milliseconds since the start of the Unix epoch
     */
    fun getTimestampNow(): Long
}