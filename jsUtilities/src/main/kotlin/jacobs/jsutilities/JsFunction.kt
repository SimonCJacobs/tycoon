package jacobs.jsutilities

class JsFunction(
    val name: String,
    private val jsCallable: dynamic
) {

    @Suppress( "UNUSED_PARAMETER", "UNUSED_VARIABLE" )
    fun apply( thisArg: Any, otherArgs: Array < Any > ): dynamic {
        val localCallable = this.jsCallable
        return js( "localCallable.apply( thisArg, otherArgs )" )
    }

}