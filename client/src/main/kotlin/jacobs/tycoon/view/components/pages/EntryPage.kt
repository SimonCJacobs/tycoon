package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.controller.UserInterfaceController
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.VNode
import org.kodein.di.Kodein
import org.kodein.di.erased.instance

class EntryPage( kodein: Kodein ) : Page {

    private val uiController: UserInterfaceController by kodein.instance()
        // TODO think this should probably be started elsewhere
    private val state : EntryPageState = uiController.getAvailablePieces()
        .let {
            EntryPageState(
                availablePieces = it,
                selectedPiece = it.random()
            )
        }

    override fun view(): VNode {
        return m( Tag.div ) {
            children(
                getPlayerEntryForm(),
                getSubmitButton(),
                m( Tag.button ) {
                    eventHandlers {
                        onclick = { uiController.triggerNetworkRequest( state ) }
                    }
                    content(  "click me!" ) },
                m( Tag.h3 ) { content( state.testText ) }
            )
        }
    }

    private fun getPlayerEntryForm() : VNode {
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
                        onInputExt = {
                            console.log(  it )
                            state.playerName = it.target.asDynamic().value as String }
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
                uiController.getAvailablePieces()
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