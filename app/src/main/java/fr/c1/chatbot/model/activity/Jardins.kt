package fr.c1.chatbot.model.activity

class Jardins(
    val region: String,
    val departement: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val codePostal: String,
    val accessible: Boolean
) : AbstractActivity() {

    override fun toString(): String {
        return "Activities(region='$region', departement='$departement', commune='$commune', nom='$nom', adresse='$adresse', codePostal='$codePostal', accessible='$accessible')"
    }
}