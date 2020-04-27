package jacobs.websockets.engine

import java.time.Instant

class JvmTimestampFactory : TimestampFactory {

    override fun getTimestampNow(): Long {
        return Instant.now().toEpochMilli()
    }

}