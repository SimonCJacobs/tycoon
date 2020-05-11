package jacobs.tycoon.domain.phases

class NotTurnOfPlayerException( message: String? = null ) : WrongPhaseException( message )

open class WrongPhaseException( message: String? = null ) : Exception( message )