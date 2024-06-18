package fr.c1.chatbot.composable

import fr.c1.chatbot.utils.LocationHandler
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun OsmdroidMapView() {
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
                val compassOverlay = CompassOverlay(
                    context,
                    InternalCompassOrientationProvider(context),
                    this
                ).apply { enableCompass() }

                overlays.add(compassOverlay)
            }
        }
    )
}