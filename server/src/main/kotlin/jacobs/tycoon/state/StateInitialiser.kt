package jacobs.tycoon.state

import jacobs.tycoon.domain.GamePhase
import jacobs.tycoon.domain.SignUp
import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.pieces.ClassicPieces
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class StateInitialiser ( kodein: Kodein ) {

    private val stateUpdateLogWrapper by kodein.instance < StateUpdateLogWrapper > ()

    suspend fun initialiseStandardGame() {
        this.stateUpdateLogWrapper.setBoard( LondonBoard() )
        this.stateUpdateLogWrapper.setPieces( ClassicPieces() )
        this.stateUpdateLogWrapper.updateStage( SignUp() )
    }

}