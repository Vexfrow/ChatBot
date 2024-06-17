package fr.c1.chatbot.model.activity

class Contenus(
    val identifiant: String,
    commune: String,
    val nom: String,
    val adresse: String,
    val lieu: String,
    val codePostal: String,
    val url: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
    ) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "culture", "art", "archives", "bibliothèque",
            "cinéma", "concert", "orchestre", "musique",
            "conférence", "danse", "exposition", "festival",
            "littérature", "musée", "patrimoine", "théâtre"
        )
    }


    override fun toString(): String {
        return "Contenu Culturel (identifiant='$identifiant', commune='$commune', nom='$nom', adresse='$adresse', lieu='$lieu', codePostal='$codePostal', url='$url', accessible='$accessible', latitude='$latitude', longitude='$longitude')"
    }
}