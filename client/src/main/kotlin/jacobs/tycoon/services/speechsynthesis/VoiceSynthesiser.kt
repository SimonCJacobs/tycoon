package jacobs.tycoon.services.speechsynthesis

import kotlin.browser.window

class VoiceSynthesiser {

    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    private val synthesiser: SpeechSynthesis = window.asDynamic().speechSynthesis as SpeechSynthesis

    fun say( text: String ) {
        val utterance = SpeechSynthesisUtterance( text )
        synthesiser.speak( utterance )
    }


}