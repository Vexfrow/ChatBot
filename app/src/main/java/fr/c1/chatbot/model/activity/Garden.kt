package fr.c1.chatbot.model.activity

class Garden(
    val region: String,
    val department: String,
    commune: String,
    val name: String,
    val address: String,
    val postalCode: String,
    val accessible: Boolean,

    ) : AbstractActivity(commune) {
    companion object {
        val passions: List<String> = listOf(
            "jardin", "fleurs", "plantes",
            "nature", "botanique", "horticulture",
            "paysage", "jardinage", "verger",
            "potager", "parc", "arboretum", "roseraie",
            "agriculture", "biologie", "écologie",
            "flore", "faune", "biodiversité"
        )
    }

    override fun toString(): String {
        return "Jardin remarquable (region='$region', departement='$department', commune='$commune', nom='$name', adresse='$address', codePostal='$postalCode', accessible='$accessible')"
    }
}