package jacobs.tycoon.domain.actions

interface ActionVisitor < T > {
    fun visit( addPlayer: AddPlayer ): T
    fun visit( completeSignUp: CompleteSignUp ): T
    fun visit( newGame: NewGame ): T
    fun visit( pieceMoved: PieceMoved ): T
    fun visit(readCard: ReadCard ): T
    fun visit( rentCharge: RentCharge ): T
    fun visit( respondToPropertyOffer: RespondToPropertyOffer ): T
    fun visit( rollForMove: RollForMove ): T
    fun visit( rollForOrder: RollForOrderAction ): T
    fun visit( setBoard: SetBoard ): T
    fun visit( setPieces: SetPieces ): T
}