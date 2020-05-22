package jacobs.tycoon.domain.phases.status

import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.phases.TurnBasedPhase
import jacobs.tycoon.domain.phases.TurnBasedPhaseVisitor
import jacobs.tycoon.domain.phases.rent.PotentialRentCharge
import jacobs.tycoon.domain.players.Player

class TurnStatus(
    initialPhase: TurnBasedPhase,
    val potentialRentCharges: List < PotentialRentCharge > = emptyList()
) : PhaseStatus {

    @PublishedApi internal val completedPhases: MutableList < TurnBasedPhase > = mutableListOf()
    @PublishedApi internal var maybeCurrentPhase: TurnBasedPhase? = initialPhase
    private val pendingPhases: ArrayDeque < TurnBasedPhase > = ArrayDeque()

    override val playerWithTurn: Player
        get() = mostRecentPhase().playerWithTurn

    // PHASE MANIPULATION API

    /**
     * Complete the current phase, and do another one when it rises to the head of the queue
     */
    fun completeAndDoAsPriority( phase: TurnBasedPhase ) {
        this.completeCurrentPhase()
        this.doAsPriority( phase )
    }

    /**
     * Complete the current phase, and do another one when it rises to the head of the queue
     */
    fun completeAndDoDuringThisTurn( phase: TurnBasedPhase ) {
        this.doPhaseDuringThisTurn( phase )
        this.completeCurrentPhaseAndDoAnyWaiting()
    }

    /**
     * Complete the current phase and do the next if there is one
     */
    fun completeCurrentPhaseAndDoAnyWaiting() {
        this.completeCurrentPhase()
        this.maybeCurrentPhase =
            if ( pendingPhases.isNotEmpty() ) pendingPhases.removeFirst()
            else null
    }

    /**
     * Put the current phase on hold and do another one as priority
     */
    fun doAsPriority( newPhase: TurnBasedPhase ) {
        maybeCurrentPhase ?.let { this.pendingPhases.addFirst( it ) }
        this.maybeCurrentPhase = newPhase
    }

    /**
     * Do phase during this turn, when it comes to the front of the queue
     */
    fun doPhaseDuringThisTurn( phase: TurnBasedPhase ) {
        this.pendingPhases.addLast( phase )
    }

    private fun completeCurrentPhase() {
        completedPhases.add( maybeCurrentPhase!! )
        this.maybeCurrentPhase = null
    }

    // PHASE ENQUIRY API

    override fun accept( phaseStatusVisitor: PhaseStatusVisitor) {
        phaseStatusVisitor.visit( this )
    }

    fun acceptVisitToCurrentPhase( phaseVisitor: TurnBasedPhaseVisitor ) {
        this.maybeCurrentPhase?.accept( phaseVisitor )
    }

    inline fun < reified T : TurnBasedPhase> anyPastPhasesOfType(): Boolean {
        return completedPhases.any { it is T }
    }

    override fun current(): TurnBasedPhase {
        return this.maybeCurrentPhase!!
    }

    inline fun < reified T : TurnBasedPhase > getFirstPhaseOfType(): T? {
        return this.completedPhases.toMutableList()
            .apply { maybeCurrentPhase?.let { add( it ) } }
            .firstOrNull { it is T } as T?
    }

    override fun isCurrent( gamePhase: GamePhase ): Boolean {
        return this.maybeCurrentPhase ?.let { it == gamePhase } ?: false
    }

    override fun isItTurnOfPlayer( testPlayer: Player ): Boolean {
        return testPlayer == playerWithTurn
    }

    fun isThereACurrentPhase(): Boolean {
        return this.maybeCurrentPhase != null
    }

    // PRIVATE METHODS

    private fun mostRecentPhase(): TurnBasedPhase {
        return this.maybeCurrentPhase ?: completedPhases.last()
    }

}