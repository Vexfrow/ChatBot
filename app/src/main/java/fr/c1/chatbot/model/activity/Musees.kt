package fr.c1.chatbot.model.activity

class Musees(
    val region: String,
    val departement: String,
    val identifiant: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val lieu: String,
    val codePostal: String,
    val telephone: String,
    val url: String,
    val accessible: Boolean
) : AbstractActivity() {

    override fun toString(): String {
        return "Activities(region='$region', departement='$departement', identifiant='$identifiant', commune='$commune', nom='$nom', adresse='$adresse', lieu='$lieu', codePostal='$codePostal', telephone='$telephone', url='$url', accessible='$accessible')"
    }
}