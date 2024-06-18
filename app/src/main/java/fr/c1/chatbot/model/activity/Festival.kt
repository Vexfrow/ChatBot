package fr.c1.chatbot.model.activity

class Festival(
    val region: String,
    val department: String,
    commune: String,
    val name: String,
    val address: String,
    val postalCode: String,
    val discipline: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
    ) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "musique", "danse", "théâtre", "cirque",
            "cinéma", "littérature", "arts plastiques",
            "photographie", "rire", "humour", "chant"
        )
    }

    override fun toString(): String {
        return "Festival(region='$region', department='$department', name='$name', address='$address', postalCode='$postalCode', discipline='$discipline', accessible=$accessible)"
    }
}