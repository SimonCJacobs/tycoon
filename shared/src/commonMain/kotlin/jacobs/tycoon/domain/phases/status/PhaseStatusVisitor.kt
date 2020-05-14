package jacobs.tycoon.domain.phases.status

interface PhaseStatusVisitor {
    fun visit( turnlessPhaseStatus: TurnlessPhaseStatus)
    fun visit( turnStatus: TurnStatus)
}
