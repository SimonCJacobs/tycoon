package jacobs.tycoon.domain.board.cards

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.players.Player

class Card(
    val instruction: String,
    val action: PhasePhactory.( Game, Player ) -> TurnBasedPhase
)