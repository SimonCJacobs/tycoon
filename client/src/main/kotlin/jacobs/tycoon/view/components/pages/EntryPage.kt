package jacobs.tycoon.view.components.pages

import jacobs.mithril.Tag
import jacobs.mithril.m
import jacobs.tycoon.clientcontroller.EntryPageController
import jacobs.tycoon.clientstate.EntryPageState
import jacobs.tycoon.domain.pieces.PlayingPiece
import org.js.mithril.VNode
import org.js.mithril.selectedIndex
import org.kodein.di.Kodein
import org.kodein.di.erased.instance
import org.w3c.dom.HTMLInputElement

class EntryPage( kodein: Kodein ) : Page {

    private val pageState by kodein.instance < EntryPageState > ()
    private val uiController by kodein.instance < EntryPageController > ()

    override fun view(): VNode {
        return if ( this.uiController.isClientWaitingForServer() )
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
                getSubmitButton(),
                getAnyMessage()
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
                        value = pageState.playerNameInProgress
                    }
                    eventHandlers {
                        onInputExt = { pageState.playerNameInProgress = ( it.target as HTMLInputElement ).value }
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
        pageState.pieceOptionList = this.uiController.getAvailablePieces()
        pageState.selectedPiece = this.uiController.getSelectedPiece( pageState.pieceOptionList )
        return m( Tag.select ) {
            eventHandlers {
                onInputExt = selectedIndex { pageState.selectedPiece = pageState.pieceOptionList[ it ] }
            }
            children(
                pageState.pieceOptionList.map { getSinglePieceOptionElement( it, pageState.selectedPiece!! ) }
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
                onclick = { uiController.onEntryPageButtonClick() }
            }
            content( "Play the game already!" )
        }
    }

    private fun getAnyMessage(): VNode? {
        if ( pageState.showNoGameEntry )
            return m( Tag.h4 ) {
                content( "Sorry. You can't join the game now" )
            }
        return null
    }

}