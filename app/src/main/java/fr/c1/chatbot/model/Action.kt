package fr.c1.chatbot.model

object Action {


    fun stringToAction(stringAction: String): TypeAction {
        return when (stringAction) {
            "EntrerDate" -> TypeAction.EntrerDate
            "EntrerDistance" -> TypeAction.EntrerDistance
            "EntrerVille" -> TypeAction.EntrerVille
            "AfficherResultat" -> TypeAction.AfficherResultat
            "Geolocalisation" -> TypeAction.Geolocalisation
            "ChoisirPassions" -> TypeAction.ChoisirPassions
            else -> TypeAction.None
        }
    }

    enum class TypeAction {
        EntrerDate, //Date picker
        EntrerDistance, //Enter entier
        EntrerVille, //Liste déroulante ? Champs de texte avec autocomplétion ?
        AfficherResultat, //à voir
        Geolocalisation, //Voir comment faire (avec demande de validation)
        ChoisirPassions, //Liste multichoix
        None //Pas d'actions
    }

}