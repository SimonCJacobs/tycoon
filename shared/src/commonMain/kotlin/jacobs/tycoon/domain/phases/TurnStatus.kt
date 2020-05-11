package jacobs.tycoon.domain.phases

import jacobs.tycoon.domain.players.Player

class TurnStatus(
    initialPhase: TurnBasedPhase
) : PhaseStatus {

    @PublishedApi internal val completedPhases: MutableList < TurnBasedPhase > = mutableListOf()
    private var maybeCurrentPhase: TurnBasedPhase? = initialPhase
    @PublishedApi internal val currentPhase: TurnBasedPhase
        get() = maybeCurrentPhase ?: throw Error ( "Accessed current phase when not in a phase" )
    private val pendingPhases: ArrayDeque < TurnBasedPhase > = ArrayDeque()

    override val playerWithTurn: Player
        get() = mostRecentPhase().playerWithTurn

    inline fun < reified T : TurnBasedPhase > anyPastPhasesOfType(): Boolean {
        return completedPhases.any { it is T }
    }

    fun completePhase() {
        completedPhases.add( currentPhase )
        maybeCurrentPhase = if ( pendingPhases.isNotEmpty() ) pendingPhases.removeFirst() else null
    }

    override fun current(): GamePhase {
        return this.currentPhase
    }

    fun currentTurnBasedPhase(): TurnBasedPhase {
        return this.currentPhase
    }

    fun doNext( nextPhase: TurnBasedPhase ) {
        this.completedPhases.add( currentPhase )
        this.maybeCurrentPhase = nextPhase
    }

    inline fun < reified T : TurnBasedPhase > getFirstPhaseOfType(): T? {
        return this.completedPhases.toMutableList().apply { add( currentPhase ) }
            .firstOrNull { it is T } as T?
    }

    override fun isItTurnOfPlayer( testPlayer: Player ): Boolean {
        return testPlayer == playerWithTurn
    }

    fun isThereAWaitingPhase(): Boolean {
        return this.maybeCurrentPhase != null
    }

    private fun mostRecentPhase(): TurnBasedPhase {
        return this.maybeCurrentPhase ?: completedPhases.last()
    }

    override fun accept( phaseStatusVisitor: PhaseStatusVisitor ) {
        phaseStatusVisitor.visit( this )
    }

}