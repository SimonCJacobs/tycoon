package jacobs.tycoon.clientcontroller

import jacobs.tycoon.domain.actions.GameAction
import jacobs.tycoon.domain.phases.GamePhase
import jacobs.tycoon.domain.services.GameCycle
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class ChangeListener (kodein: Kodein ) {

    private val gameCycle by kodein.instance < GameCycle >()

    private val actionListeners: MutableList < (GameAction ) -> Unit > = mutableListOf()
    private val phaseChangeListeners: MutableList < ( GamePhase ) -> Unit > = mutableListOf()


    init {
        gameCycle.registerPhaseChangeListener { onPhaseChange( it ) }
    }

    // LISTENER FUNCTIONS

    fun onAction( gameAction: GameAction ) {
        this.actionListeners.forEach { it( gameAction ) }
    }

    @Suppress(  "MemberVisibilityCanBePrivate" )
    fun onPhaseChange( newPhase: GamePhase ) {
        this.phaseChangeListeners.forEach { it( newPhase ) }
    }

    // REGISTRATION FUNCTIONS

    fun registerActionListener( listener: (GameAction ) -> Unit ) {
        this.actionListeners.add( listener )
    }

    fun registerPhaseChangeListener( listener: ( GamePhase ) -> Unit ) {
        this.phaseChangeListeners.add( listener )
    }

}