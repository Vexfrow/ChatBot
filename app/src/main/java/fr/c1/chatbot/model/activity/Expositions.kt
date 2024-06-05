package fr.c1.chatbot.model.activity

class Expositions(
    val region: String,
    val departement: String,
    val identifiant: String,
    val commune: String,
    val nom: String,
    val url: String,
    val accessible: Boolean,

    ) : AbstractActivity() {
    companion object {
        val passions: List<String> = listOf(
            "exposition", "musée", "peinture", "sculpture",
            "photographie", "art", "histoire", "culture",
            "patrimoine", "visite", "visite guidée", "visite libre",
            "visite commentée", "visite virtuelle", "exposition virtuelle",
        )
    }

    override fun toString(): String {
        return "Exposition (region='$region', departement='$departement', identifiant='$identifiant', commune='$commune', nom='$nom', url='$url', accessible='$accessible')"
    }
}