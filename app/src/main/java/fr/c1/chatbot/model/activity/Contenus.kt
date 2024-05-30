package fr.c1.chatbot.model.activity

class Contenus(
    val identifiant: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val lieu: String,
    val codePostal: String,
    val url: String,
    val accessible: Boolean,
    passions: List<String> = listOf(
        "culture", "art", "archives", "bibliothèque",
        "cinéma", "concert", "orchestre", "musique",
        "conférence", "danse", "exposition", "festival",
        "littérature", "musée", "patrimoine", "théâtre"
    )
) : AbstractActivity() {

    override fun toString(): String {
        return "Contenu Culturel (identifiant='$identifiant', commune='$commune', nom='$nom', adresse='$adresse', lieu='$lieu', codePostal='$codePostal', url='$url', accessible='$accessible')"
    }
}