package fr.c1.chatbot.model.activity

class Contenus(
    val identifiant: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val lieu: String,
    val codePostal: String,
    val url: String,
    val accessible: Boolean
) : AbstractActivity() {

    override fun toString(): String {
        return "Activities(identifiant='$identifiant', commune='$commune', nom='$nom', adresse='$adresse', lieu='$lieu', codePostal='$codePostal', url='$url', accessible='$accessible')"
    }
}