package jacobs.tycoon.domain.phases

interface PhaseStatusVisitor {
    fun visit( turnlessPhaseStatus: TurnlessPhaseStatus )
    fun visit( turnStatus: TurnStatus )
}
