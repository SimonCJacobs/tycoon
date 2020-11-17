package jacobs.tycoon.view.components.players

import jacobs.tycoon.clientcontroller.AdminController
import jacobs.tycoon.clientstate.AdminState
import jacobs.tycoon.domain.players.Player
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class AdminPlayerComponentReposifactory( kodein: Kodein ) : PlayerComponentReposifactory( kodein ) {

    private val adminController by kodein.instance < AdminController >()
  //  private val adminState by kodein.instance < AdminState >()

    override fun newPassiveSinglePlayerComponent( player: Player ): PassiveSinglePlayerComponent {
        return AdminPassiveSinglePlayerComponent(
            adminController,
          //  adminState,
            gameState,
            pieceDisplayStrategy,
            controllerProvider( player ),
            player
        )
    }

}