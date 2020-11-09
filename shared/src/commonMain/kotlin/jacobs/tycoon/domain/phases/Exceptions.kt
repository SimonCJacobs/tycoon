package jacobs.tycoon.domain.phases

abstract class BadActionException( message: String? ) : Exception( message )

class NotTurnOfPlayerException( message: String? = null ) : BadActionException( message )

class WrongPhaseException( message: String? = null ) : BadActionException( message )