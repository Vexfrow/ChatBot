package fr.c1.chatbot.composable

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import fr.c1.chatbot.model.activity.AbstractActivity
import fr.c1.chatbot.model.activity.Association
import fr.c1.chatbot.model.activity.Building
import fr.c1.chatbot.model.activity.Content
import fr.c1.chatbot.model.activity.Exposition
import fr.c1.chatbot.model.activity.Festival
import fr.c1.chatbot.model.activity.Garden
import fr.c1.chatbot.model.activity.Museum
import fr.c1.chatbot.model.activity.Site
import fr.c1.chatbot.utils.LocationHandler
import fr.c1.chatbot.utils.Resource
import fr.c1.chatbot.viewModel.ActivitiesVM
import org.osmdroid.api.IGeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme


@Composable
fun OsmdroidMapView(aVM: ActivitiesVM) {

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
                            LocationHandler.currentLocation!!.latitude,
                            LocationHandler.currentLocation!!.longitude
                        )
                    )
                }
                val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this)
                mLocationOverlay.enableMyLocation()
                overlays.apply {
                    // create overlays with differents themes
                    // add overlays
                    setResult(aVM.result)
                    add(setSFPO(festivalsLocations, "#ff4f29"))
                    add(setSFPO(associationsLocations, "#e478ff"))
                    add(setSFPO(museesLocations, "#cecece"))
                    add(setSFPO(expositionsLocations, "#2db0ff"))
                    add(setSFPO(sitesLocations, "#ffb02d"))
                    add(setSFPO(contenuLocations, "#fffc93"))
                    add(setSFPO(edificesLocations, "#b87800"))
                    add(setSFPO(jardinLocations, "#6cff40"))
                    add(mLocationOverlay)
                }
            }
        }
    )
}

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
fun Paint.setTextStyle(color: String) {
    style = Paint.Style.FILL
    this.color = Color.parseColor(color)
    textAlign = Paint.Align.CENTER
    textSize = 24f
}

private fun setResult(res: Resource<List<AbstractActivity>>) {
    res.data?.forEach {
        when (it) {
            is Association -> {
                associationsLocations.add(
                    LabelledGeoPoint(it.latitude, it.longitude, it.name )
                )
            }

            is Museum -> {
                museesLocations.add(
                    LabelledGeoPoint(it.latitude, it.longitude, it.name)
                )
            }

            is Exposition -> {
                expositionsLocations.add(
                    LabelledGeoPoint(it.latitude, it.longitude, it.name)
                )
            }

            is Site -> {
                sitesLocations.add(
                    LabelledGeoPoint(it.latitude, it.longitude, it.commune)
                )
            }

            is Content -> {
                contenuLocations.add(
                    LabelledGeoPoint(it.latitude, it.longitude, it.name)
                )
            }

            is Building -> {
                edificesLocations.add(
                    LabelledGeoPoint(it.latitude, it.longitude, it.name)
                )
            }

            is Garden -> {
                jardinLocations.add(
                    LabelledGeoPoint(it.latitude, it.longitude, it.name)
                )
            }

            is Festival -> {
                festivalsLocations.add(
                    LabelledGeoPoint(it.latitude, it.longitude, it.name)
                )
            }
        }
    }
}

fun setSFPO(points: ArrayList<IGeoPoint>, color: String): SimpleFastPointOverlay {
    val textStyle = Paint()
    val pointStyle = Paint()
    textStyle.setTextStyle(color)
    // wrap them in a theme
    val pt = SimplePointTheme(points, true)
    // set some visual options for the overlay
    // we use here MAXIMUM_OPTIMIZATION algorithm, which works well with >100k points
    val opt = SimpleFastPointOverlayOptions.getDefaultStyle()
        .setSymbol(SimpleFastPointOverlayOptions.Shape.CIRCLE)
        .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
        .setRadius(7f).setIsClickable(true).setCellSize(15).setTextStyle(textStyle).setPointStyle(textStyle).setMinZoomShowLabels(10)

    return SimpleFastPointOverlay(pt, opt)
}