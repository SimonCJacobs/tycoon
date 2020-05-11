package jacobs.tycoon.view.components.pieces

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.Component
import org.js.mithril.VNode

class ClassicPieceEmojiDisplayStrategy : PieceDisplayStrategy {

    override fun getPieceDisplayComponent( playingPiece: PlayingPiece ): Component {
        return object : Component {
            override fun view(): VNode {
                return m( Tag.h6 ) { content( getEmojiFromPiece( playingPiece ) ) }
            }
        }
    }

    private fun getEmojiFromPiece( playingPiece: PlayingPiece ): String {
        return when ( playingPiece.name ) {
            "Battleship" -> "🚢"
            "Boot" -> "👢"
            "Cannon" -> "🔫"
            "Horse and rider" -> "🏇"
            "Iron" -> "🧲"
            "Racing car" -> "🏎️"
            "Scottie dog" -> "🐕"
            "Thimble" -> "🧵"
            "Top hat" -> "🎩"
            "Wheelbarrow" -> "🥕"
            else -> throw Error( "Missing piece name ${ playingPiece.name }")
        }
    }

}