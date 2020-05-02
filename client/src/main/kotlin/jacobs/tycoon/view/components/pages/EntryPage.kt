package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.UserInterfaceController
import jacobs.tycoon.clientstate.EntryPageState
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.VNode
import org.js.mithril.selectedIndex
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.w3c.dom.HTMLInputElement

class EntryPage( kodein: Kodein ) : Page {

    private val state by kodein.instance < EntryPageState > ()
    private val uiController by kodein.instance < UserInterfaceController > ()

    override fun view(): VNode {
        return if ( this.uiController.isAppWaitingForServer() )
            this.displayWaitingScreen()
        else
            this.getPlayerEntryForm()
    }

    private fun displayWaitingScreen(): VNode {
        return m( Tag.h1 ){ content( "Waiting for server" ) }
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
                        onInputExt = {
                            console.log( "entered name" );state.playerName = ( it.target as HTMLInputElement ).value }
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
        state.pieceOptionList = this.uiController.getAvailablePieces()
        state.selectedPiece = this.uiController.getSelectedPiece( state.pieceOptionList )
        return m( Tag.select ) {
            eventHandlers {
                onInputExt = selectedIndex { state.selectedPiece = state.pieceOptionList[ it ]
                console.log( "selected piece is now ${ state.selectedPiece }")}
            }
            children(
                state.pieceOptionList.map { getSinglePieceOptionElement( it, state.selectedPiece!! ) }
            )
        }
    }

    private fun getSinglePieceOptionElement( thisPiece: PlayingPiece, selectedPiece: PlayingPiece ): VNode {
        return m( Tag.option ) {
            attributes {
                value = thisPiece.name
                selected = if ( thisPiece == selectedPiece ) "selected" else ""
            }
            content( thisPiece.name )
        }
    }

    private fun getSubmitButton(): VNode {
        return m( Tag.button ) {
            attributes {
                type = "button"
            }
            eventHandlers {
                onclick = { console.log( "clicked" ); uiController.onEntryPageButtonClick() }
            }
            content( "Play the game already!" )
        }
    }

}