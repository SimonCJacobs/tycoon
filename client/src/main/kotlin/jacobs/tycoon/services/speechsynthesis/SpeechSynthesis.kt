package jacobs.tycoon.services.speechsynthesis

external interface SpeechSynthesis {
    fun speak( utterance: SpeechSynthesisUtterance )
}