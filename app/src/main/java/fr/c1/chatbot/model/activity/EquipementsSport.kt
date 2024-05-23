package fr.c1.chatbot.model.activity

class EquipementsSport(
    val departement: String,
    val identifiant: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val codePostal: String,
    val url: String,
    val accessible: Boolean) {

    override fun toString(): String {
        return "Activities(departement='$departement', identifiant='$identifiant', commune='$commune', nom='$nom', adresse='$adresse', codePostal='$codePostal', url='$url', accessible='$accessible')"
    }
}