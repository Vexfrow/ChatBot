package fr.c1.chatbot.model.activity

class Edifices(
    val region: String,
    val departement: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val accessible: Boolean,
    passions: List<String> = listOf(
        "architecture", "contemporaine", "édifices",
        "design", "moderne", "bâtiments", "construction",
        "urbanisme", "ville", "habitat", "logement",
        "immeubles", "maisons", "histoire"
    )
) : AbstractActivity() {

    override fun toString(): String {
        return "Edifice avec architecture contemporaine (region='$region', departement='$departement', commune='$commune', nom='$nom', adresse='$adresse', accessible='$accessible')"
    }
}