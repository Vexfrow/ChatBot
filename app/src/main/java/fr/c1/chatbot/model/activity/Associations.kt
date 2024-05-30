package fr.c1.chatbot.model.activity

class Associations(
    val departement: String,
    val identifiant: String,
    val commune: String,
    val nom: String,
    val adresse: String,
    val codePostal: String,
    val accessible: Boolean,
    passions: List<String> = listOf(
        "association", "bénévolat", "engagement", "solidarité",
        "humanitaire", "social", "culture", "sport",
        "environnement", "éducation", "santé", "handicap",
        "économie", "emploi", "formation", "insertion",
        "logement", "justice", "citoyenneté", "développement",
        "international", "numérique", "innovation", "égalité",
        "diversité", "inclusion", "démocratie", "participation",
        "coopération", "échanges", "mobilité", "communication",
        "information", "médiation", "animation", "événementiel",
        "formation", "séminaire", "colloque", "visite",
        "rencontre", "échange", "partage", "découverte",
        "initiation", "santé", "prévention", "accompagnement", "écoute"
    )
) : AbstractActivity() {

    override fun toString(): String {
        return "Association (departement='$departement', identifiant='$identifiant', commune='$commune', nom='$nom', adresse='$adresse', codePostal='$codePostal', accessible='$accessible')"
    }
}