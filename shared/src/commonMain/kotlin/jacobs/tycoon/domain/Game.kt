package jacobs.tycoon.domain

import jacobs.tycoon.domain.logs.ActionLog
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class Game( kodein: Kodein ) {
    private val loggo: ActionLog by kodein.instance()
    var message = "Pluto"
    var num = 0
}