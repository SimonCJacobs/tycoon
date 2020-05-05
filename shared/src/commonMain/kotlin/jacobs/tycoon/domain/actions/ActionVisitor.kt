package jacobs.tycoon.domain.actions

interface ActionVisitor < T > {
    fun visit( addPlayer: AddPlayer ): T
    fun visit( completeSignUp: CompleteSignUp ): T
    fun visit( newGame: NewGame ): T
    fun visit( rollForOrder: RollForOrder ): T
    fun visit( setBoard: SetBoard ): T
    fun visit( setPieces: SetPieces ): T
}