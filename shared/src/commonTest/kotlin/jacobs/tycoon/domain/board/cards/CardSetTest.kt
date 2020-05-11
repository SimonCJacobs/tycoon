package jacobs.tycoon.domain.board.cards

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.test.Test
import kotlin.test.asserter

class CardSetTest {

    @Test
    fun canDrawCard() {
        val communityChestCards = CommunityChestCards()
        communityChestCards.shuffle()
        val card = communityChestCards.drawCard()
        asserter.assertEquals( "Returns instance of class Card", Card::class, card::class )
    }

    @Test
    fun serializesAndDeserializesCommunityChestAsCardSet() {
        val communityChestCards = CommunityChestCards()
        val json = Json(
            JsonConfiguration.Stable,
            context = cardsSerializerModule()
        )
        val serializedCards = json.stringify( CardSet.serializer(), communityChestCards )
        val deserializedCards = json.parse( CardSet.serializer(), serializedCards )
        asserter.assertEquals(
            "Deserializes to same class",
            CommunityChestCards::class,
            deserializedCards::class
        )
    }

}