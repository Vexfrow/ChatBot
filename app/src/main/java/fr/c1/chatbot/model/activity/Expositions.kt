package fr.c1.chatbot.model.activity

class Expositions(
    val region: String,
    val departement: String,
    val identifiant: String,
    val commune: String,
    val nom: String,
    val url: String,
    val accessible: Boolean) {

    override fun toString(): String {
        return "Activities(region='$region', departement='$departement', identifiant='$identifiant', commune='$commune', nom='$nom', url='$url', accessible='$accessible')"
    }
}