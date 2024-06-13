package fr.c1.chatbot.model.activity

class Building(
    val region: String,
    val department: String,
    commune: String,
    val name: String,
    val address: String,
    val accessible: Boolean,

    ) : AbstractActivity(commune) {
    companion object {
        val passions: List<String> = listOf(
            "architecture", "contemporaine", "édifices",
            "design", "moderne", "bâtiments", "construction",
            "urbanisme", "ville", "habitat", "logement",
            "immeubles", "maisons", "histoire"
        )
    }

    override fun toString(): String {
        return "Edifice avec architecture contemporaine (region='$region', departement='$department', commune='$commune', nom='$name', adresse='$address', accessible='$accessible')"
    }
}