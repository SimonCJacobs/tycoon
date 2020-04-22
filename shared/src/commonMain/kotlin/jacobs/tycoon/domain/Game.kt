package jacobs.tycoon.domain

import jacobs.tycoon.domain.logs.ActionLog

class Game( private val loggo: ActionLog ) {
    var message = "Pluto"
    var num = 0
}