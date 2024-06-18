package fr.c1.chatbot.model.activity

class Content(
    val id: String,
    commune: String,
    val name: String,
    val address: String,
    val location: String,
    val postalCode: String,
    val url: String,
    val accessible: Boolean,

    ) : AbstractActivity(commune) {
    companion object {
        val passions: List<String> = listOf(
            "culture", "art", "archives", "bibliothèque",
            "cinéma", "concert", "orchestre", "musique",
            "conférence", "danse", "exposition", "festival",
            "littérature", "musée", "patrimoine", "théâtre"
        )
    }


    override fun toString(): String {
        return "Contenu Culturel (identifiant='$id', commune='$commune', nom='$name', adresse='$address', lieu='$location', codePostal='$postalCode', url='$url', accessible='$accessible')"
    }
}