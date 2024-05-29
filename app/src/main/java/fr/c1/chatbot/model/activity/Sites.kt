package fr.c1.chatbot.model.activity

class Sites(
    val region: String,
    val departement: String,
    val commune: String,
    val accessible: Boolean
) : AbstractActivity() {

    override fun toString(): String {
        return "Activities(region='$region', departement='$departement', commune='$commune', accessible='$accessible')"
    }
}