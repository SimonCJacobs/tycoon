package jacobs.tycoon.controller.communication

interface RequestVisitor < T > {
    fun visit( simpleRequestWrapper: SimpleRequestWrapper ): T
}