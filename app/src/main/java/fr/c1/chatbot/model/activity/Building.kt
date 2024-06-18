package fr.c1.chatbot.model.activity

class Building(
    val region: String,
    val department: String,
    commune: String,
    val name: String,
    val address: String,
    val accessible: Boolean,
    latitude: Double,
    longitude: Double
    ) : AbstractActivity(commune, latitude, longitude) {
    companion object {
        val passions: List<String> = listOf(
            "architecture", "contemporaine", "édifices",
            "design", "moderne", "bâtiments", "construction",
            "urbanisme", "ville", "habitat", "logement",
            "immeubles", "maisons", "histoire"
        )
    }

    override fun toString(): String {
        return "Building(region='$region', department='$department', name='$name', address='$address', accessible=$accessible)"
    }
}