package jacobs.tycoon.domain.board.cards

import jacobs.tycoon.domain.board.LondonBoard
import jacobs.tycoon.domain.board.StandardBoard
import jacobs.tycoon.domain.board.currency.Currency
import jacobs.tycoon.domain.board.currency.PoundsSterling
import jacobs.tycoon.domain.rules.MiscellaneousRules
import jacobs.tycoon.domain.rules.StandardMiscellaneousRules
import kotlinx.serialization.json.Json
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import kotlin.test.Test
import kotlin.test.asserter

class CardSetTest {

    @Test
    fun canDrawCard() {
        val communityChestCards = this.getNewInstance()
        communityChestCards.shuffle()
        val card1 = communityChestCards.drawCard()
        val card2 = communityChestCards.drawCard()
        asserter.assertNotEquals( "Returns two different cards", card1.instruction, card2.instruction )
    }

    @Test
    fun serializesAndDeserializesCommunityChestAsCardSet() {
        val communityChestCards = this.getNewInstance()
        val json = Json {
            serializersModule = cardsSerializerModule()
        }
        val serializedCards = json.encodeToString( CardSet.serializer(), communityChestCards )
        val deserializedCards = json.decodeFromString( CardSet.serializer(), serializedCards )
        asserter.assertEquals(
            "Deserializes to same class",
            CommunityChestCards::class,
            deserializedCards::class
        )
    }

    private fun getNewInstance(): CardSet {
        val kodein = Kodein {
            bind < Currency >() with instance( PoundsSterling() )
        }
        return CommunityChestCards.fromBoardAndRules(
            LondonBoard( PoundsSterling() ), StandardMiscellaneousRules( kodein ), StandardBoard.CHANCE_CARDS_NAME
        )
    }

}