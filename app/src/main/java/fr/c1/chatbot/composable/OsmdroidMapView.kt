package fr.c1.chatbot.composable

import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
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
import fr.c1.chatbot.utils.LocationHandler.currentLocation
import fr.c1.chatbot.utils.Resource
import fr.c1.chatbot.viewModel.ActivitiesVM
import org.osmdroid.api.IGeoPoint
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ClickableIconOverlay
import org.osmdroid.views.overlay.OverlayWithIW
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme
import androidx.compose.ui.graphics.Color as ColorX

private const val TAG = "OsmdroidMapView"

class CustomInfoWindow(private val mapView: MapView) : InfoWindow(
    ComposeView(mapView.context), mapView
) {
    override fun onOpen(item: Any?) {
        // Following command
        closeAllInfoWindowsOn(mapView)
//
//        val title = mView.findViewById<TextView>(R.id.title)
//        val snippet = mView.findViewById<TextView>(R.id.snippet)
//
//        // Assurez-vous que l'objet item est de type adéquat (ici on suppose qu'il est de type Point)
        val point = item as LabelledGeoPoint
//
//        title.text = point.label
//
//        // You can set an onClickListener on the InfoWindow itself.
//        // This is so that you can close the InfoWindow once it has been tapped.
//
//        // Instead, you could also close the InfoWindows when the map is pressed.
//        // This is covered in the Map Listeners guide.
//
        mView.setOnClickListener {
            close()
        }

        (mView as ComposeView).setContent {
            Box { Text(text = point.label, color = ColorX.Black) }
        }
    }

    override fun onClose() {
        mapView.unselectAllPoints()
    }
}

private fun MapView.unselectAllPoints() {
    for (overlay in overlays) {
        if (overlay !is SimpleFastPointOverlay)
            return

        overlay.selectedPoint = -1
    }
}
@Composable
fun OsmdroidMapView(aVM: ActivitiesVM) {
    // Variable pour stocker la MapView
    var mapView by remember { mutableStateOf<MapView?>(null) }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            MapView(context).apply mv@{
                mapView = this
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
                val mLocationOverlay = MyLocationNewOverlay(this)
                mLocationOverlay.enableMyLocation()

                overlays.apply {
                    // create overlays with differents themes
                    // add overlays
                    setResult(aVM.result)
                    add(setSFPO(this@mv, festivalsLocations, "#ff4f29"))
                    add(setSFPO(this@mv, associationsLocations, "#e478ff"))
                    add(setSFPO(this@mv, museesLocations, "#cecece"))
                    add(setSFPO(this@mv, expositionsLocations, "#2db0ff"))
                    add(setSFPO(this@mv, sitesLocations, "#ffb02d"))
                    add(setSFPO(this@mv, contenuLocations, "#fffc93"))
                    add(setSFPO(this@mv, edificesLocations, "#b87800"))
                    add(setSFPO(this@mv, jardinLocations, "#6cff40"))
                    add(mLocationOverlay)
                }

                addMapListener(object : MapListener {
                    override fun onScroll(event: ScrollEvent?): Boolean {
                        InfoWindow.closeAllInfoWindowsOn(this@mv)
                        return true
                    }

                    override fun onZoom(event: ZoomEvent?): Boolean {
                        InfoWindow.closeAllInfoWindowsOn(this@mv)
                        return true
                    }
                })
            }
        }

    )
    // Bouton en bas de la carte
    IconButton(
        onClick = {
            // Action à réaliser lorsqu'on clique sur le bouton
            // Vous pouvez interagir avec la MapView ici si nécessaire
            mapView?.controller?.setCenter(
                GeoPoint(
                    LocationHandler.currentLocation!!.latitude,
                    LocationHandler.currentLocation!!.longitude
                )
            )
            // Rafraîchir la carte pour afficher les modifications
            mapView?.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MyLocation,
            contentDescription = ""
        )
    }

}

object Obj : OverlayWithIW() {
    fun foo() {
        closeInfoWindow()
    }
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
                    LabelledGeoPoint(it.latitude, it.longitude, it.name)
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

fun setSFPO(mapView: MapView, points: ArrayList<IGeoPoint>, color: String): SimpleFastPointOverlay {
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
        .setRadius(7f).setIsClickable(true).setCellSize(15).setTextStyle(textStyle)
        .setPointStyle(textStyle).setMinZoomShowLabels(15)
    val sfpo = SimpleFastPointOverlay(pt, opt)
    // onClick callback

    sfpo.setOnClickListener { pointsAdptr, i ->
        val infoWindow = CustomInfoWindow(mapView)
        infoWindow.open(pointsAdptr[i], pointsAdptr[i] as GeoPoint, 0, 0)
    }
    return sfpo
}