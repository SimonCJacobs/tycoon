package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.UserInterfaceController
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class EntryPage( kodein: Kodein ) : Page {

    private val uiController by kodein.instance <UserInterfaceController> ()
    private val state : EntryPageState = EntryPageState( uiController.getAvailablePiecesAsync() )

    override fun view(): VNode {
        return if ( ! state.isReady )
            m( Tag.h1 ){ content( "Waiting for server" ) }
        else
            this.getPlayerEntryForm()
    }

    private fun getPlayerEntryForm() : VNode {
        return m( Tag.div ) {
            children(
                getPlayerEntryFields(),
                getSubmitButton()
            )
        }
    }

    private fun getPlayerEntryFields() : VNode {
        return m( Tag.form ) {
            children(
                getPlayerNameEntry(),
                getPlayerPieceSelection()
            )
        }
    }

    private fun getPlayerNameEntry(): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.label ) {
                    content( "What's your name?" )
                },
                m( Tag.input ) {
                    attributes {
                        type = text
                        value = state.playerName
                    }
                    eventHandlers {
                        onInputExt = { state.playerName = it.target.asDynamic().value as String }
                    }
                }
            )
        }
    }

    private fun getPlayerPieceSelection(): VNode {
        return m( Tag.div ) {
            children(
                m( Tag.label ) {
                    content( "What piece would you like?" )
                },
                getPieceDropdown()
            )
        }
    }

    private fun getPieceDropdown(): VNode {
        return m( Tag.select ) {
            eventHandlers {
                onInputExt = {
                    e -> ( e.target.asDynamic().selectedIndex as Int )
                        .let { state.selectedPiece = state.availablePieces[ it ] }
                    }
            }
            children(
                state.availablePieces
                    .map { getNamedPieceOptionElement( it ) }
            )
        }
    }

    private fun getSubmitButton(): VNode {
        return m( Tag.button ) {
            attributes {
                type = "button"
            }
            eventHandlers {
                onclick = { uiController.onEntryPageButtonClick( state ) }
            }
            content( "Play the game already!" )
        }
    }

    private fun getNamedPieceOptionElement( piece: PlayingPiece ): VNode {
        return m( Tag.option ) {
            attributes {
                value = piece.name
                selected = if ( piece == state.selectedPiece ) "selected" else ""
            }
            content( piece.name )
        }
    }

}