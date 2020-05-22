package jacobs.tycoon.domain.board.cards

import jacobs.tycoon.domain.phases.PhasePhactory
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.players.Player

typealias CardAction = PhasePhactory.( Player ) -> TurnBasedPhase?