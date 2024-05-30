package fr.c1.chatbot.model.activity

class Edifices(
    val region: String,
    val departement: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val accessible: Boolean
) : AbstractActivity() {

    override fun toString(): String {
        return "Activities(region='$region', departement='$departement', commune='$commune', nom='$nom', adresse='$adresse', accessible='$accessible')"
    }
}