package fr.c1.chatbot.model.activity

/**
 * Represents a building with a contemporary design.
 * @param region the region where the building is located
 * @param department the department where the building is located
 * @param commune the commune where the building is located
 * @param name the name of the building
 * @param address the address of the building
 * @param accessible true if the association is accessible, false otherwise
 * @param latitude the latitude of the building
 * @param longitude the longitude of the building
 * @constructor creates a building activity
 * @see AbstractActivity
 */
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