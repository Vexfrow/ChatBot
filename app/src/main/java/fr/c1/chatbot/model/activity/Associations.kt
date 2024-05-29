package fr.c1.chatbot.model.activity

class Associations(
    val departement: String,
    val identifiant: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val codePostal: String,
    val accessible: Boolean
) : AbstractActivity() {

    override fun toString(): String {
        return "Activities(departement='$departement', identifiant='$identifiant', commune='$commune', nom='$nom', adresse='$adresse', codePostal='$codePostal', accessible='$accessible')"
    }
}