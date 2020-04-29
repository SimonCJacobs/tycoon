package jacobs.tycoon.controller.communication

interface RequestVisitor < T > {
    fun visit( addPlayerRequest: AddPlayerRequest ): T
    fun visit( simpleRequestWrapper: SimpleRequestWrapper ): T
}