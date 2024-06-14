package fr.c1.chatbot.utils

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import fr.c1.chatbot.model.ActivitiesRepository
import fr.c1.chatbot.model.activity.AbstractActivity
import fr.c1.chatbot.model.activity.Associations
import fr.c1.chatbot.model.activity.Contenus
import fr.c1.chatbot.model.activity.Edifices
import fr.c1.chatbot.model.activity.EquipementsSport
import fr.c1.chatbot.model.activity.Expositions
import fr.c1.chatbot.model.activity.Festivals
import fr.c1.chatbot.model.activity.Jardins
import fr.c1.chatbot.model.activity.Musees
import fr.c1.chatbot.model.activity.Sites
import fr.c1.chatbot.model.activity.Type
import org.osmdroid.api.IGeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme

// create 10k labelled points
// in most cases, there will be no problems of displaying >100k points, feel free to try
var festivalsLocations = ArrayList<IGeoPoint>()
var associationsLocations = ArrayList<IGeoPoint>()
var museesLocations = ArrayList<IGeoPoint>()
var expositionsLocations = ArrayList<IGeoPoint>()
var sitesLocations = ArrayList<IGeoPoint>()
var contenuLocations = ArrayList<IGeoPoint>()
var edificesLocations = ArrayList<IGeoPoint>()
var jardinLocations = ArrayList<IGeoPoint>()
// create label style
fun setTextStyle(textStyle:Paint, color : String){
    textStyle.style = Paint.Style.FILL
    textStyle.color = Color.parseColor(color)
    textStyle.textAlign = Paint.Align.CENTER
    textStyle.textSize = 24f
}

private fun setResult(res: List<AbstractActivity>){
    when (res::class) {
        Associations::class -> {
            /*associationsLocations.add(
                LabelledGeoPoint(res.x, res.y, res.nom )
            )*/
        }
        Musees::class -> {
            /*museesLocations.add(
                LabelledGeoPoint(res.x, res.y, res.nom )
            )*/
        }
        Expositions::class -> {
            /*expositionsLocations.add(
                LabelledGeoPoint(res.x, res.y, res.nom )
            )*/
        }
        Sites::class -> {
            /*sitesLocations.add(
                LabelledGeoPoint(res.x, res.y, res.nom )
            )*/
        }
        Contenus::class -> {
            /*contenuLocations.add(
                LabelledGeoPoint(res.x, res.y, res.nom )
            )*/
        }
        Edifices::class -> {
            /*edificesLocations.add(
                LabelledGeoPoint(res.x, res.y, res.nom )
            )*/
        }
        Jardins::class -> {
            /*jardinLocations.add(
                LabelledGeoPoint(res.x, res.y, res.nom )
            )*/
        }
        Festivals::class -> {
            /*festivalsLocations.add(
                LabelledGeoPoint(res.x, res.y, res.nom )
            )*/
        }
    }
}
fun setResultPoints(ar : ActivitiesRepository){
    //setResult(ar.resultats)

}
fun setSFPO(points :ArrayList<IGeoPoint>, color:String): SimpleFastPointOverlay{
    val textStyle = Paint()
    setTextStyle(textStyle, color)
    // wrap them in a theme
    val pt = SimplePointTheme(points, true)

    // set some visual options for the overlay
    // we use here MAXIMUM_OPTIMIZATION algorithm, which works well with >100k points
    val opt = SimpleFastPointOverlayOptions.getDefaultStyle()
        .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
        .setRadius(7f).setIsClickable(true).setCellSize(15).setTextStyle(textStyle)
    val sfpo = SimpleFastPointOverlay(pt, opt)
    return sfpo
}
@Composable
fun OsmdroidMapView(locationHandler : LocationHandler, ar : ActivitiesRepository) {

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)

                setMultiTouchControls(true)
                controller.apply {
                    setZoom(15.0)
                    setCenter(
                        GeoPoint(
                            locationHandler.currentLocation!!.latitude,
                            locationHandler.currentLocation!!.longitude
                        )
                    )
                }
                val compassOverlay = CompassOverlay(
                    context,
                    InternalCompassOrientationProvider(context),
                    this
                ).apply { enableCompass() }

                overlays.add(compassOverlay)
                // create overlays with differents themes
                // add overlays
                //setResult(ar.resultats)
                overlays.add(setSFPO(festivalsLocations, "#ff4f29"))
                overlays.add(setSFPO(associationsLocations, "#e478ff"))
                overlays.add(setSFPO(museesLocations, "#cecece"))
                overlays.add(setSFPO(expositionsLocations, "#2db0ff"))
                overlays.add(setSFPO(sitesLocations, "#ffb02d"))
                overlays.add(setSFPO(contenuLocations, "#fffc93"))
                overlays.add(setSFPO(edificesLocations, "#b87800"))
                overlays.add(setSFPO(jardinLocations, "#6cff40"))


            }
        }
    )
}