package jacobs.tycoon.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class GamePhase {

    abstract val name: String

    override fun equals( other: Any? ): Boolean {
        if ( other == null )
            return false
        else
            return this::class == other::class
    }

    override fun hashCode(): Int {
        return this.name.hashCode()
    }

}

@Serializable
class SignUp : GamePhase() {
    @Transient override val name = "sign-up"
}

@Serializable
class InPlay : GamePhase() {
    @Transient override val name = "normal play"
}