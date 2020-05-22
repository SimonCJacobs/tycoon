package jacobs.tycoon.domain.services

import jacobs.tycoon.domain.Game
import jacobs.tycoon.domain.bank.Bank
import jacobs.tycoon.domain.rules.JailRules
import jacobs.tycoon.domain.rules.MiscellaneousRules
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class GameFactory( kodein: Kodein ) {

    private val jailRules by kodein.instance < JailRules > ()
    private val miscellaneousRules by kodein.instance < MiscellaneousRules > ()

    fun newGame(): Game {
        val bank = Bank( miscellaneousRules )
        return Game( bank, miscellaneousRules, jailRules )
    }

}