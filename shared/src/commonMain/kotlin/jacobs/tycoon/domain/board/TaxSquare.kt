package jacobs.tycoon.domain.board

import kotlinx.serialization.Serializable

@Serializable
class TaxSquare ( override val name: String ) : ActionSquare() {

}