package jacobs.websockets.engine

import kotlin.js.Date
import kotlin.math.roundToLong

class JsTimestampFactory : TimestampFactory {

    override fun getTimestampNow(): Long {
        return ( Date.now() * 1000 ).roundToLong()
    }

}