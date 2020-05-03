package jacobs.tycoon.view.components.players

import jacobs.tycoon.domain.players.Player

typealias NamedPlayerProvider = ( String ) -> Player?
typealias PlayerProvider = () -> Player?