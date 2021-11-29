package com.utnfrbamobile.runnity.ui


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.data.LocationEntity
import com.utnfrbamobile.runnity.data.RunnityDao
import com.utnfrbamobile.runnity.data.RunnityDaoSingleton
import com.utnfrbamobile.runnity.domain.FetchLocationUseCase
import com.utnfrbamobile.runnity.work.RunnityWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

class MapFragment : Fragment(), OnMapReadyCallback {

    private var map : GoogleMap? = null

    private lateinit var runnityDao: RunnityDao
    private lateinit var fetchLocationUseCase : FetchLocationUseCase
    private var marker: Marker? = null
    private var polyline: Polyline? = null

    private var previousLatitude: Double = 0.0
    private var previousLongitude: Double = 0.0

    private var distance: Float = 0F
    private lateinit var distanceTextView: TextView

    private val listenLocationUpdates = Observer { newLocations: List<LocationEntity> ->
        drawPrimaryLinePath(newLocations)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    private fun bind() = context?.let {
        runnityDao = RunnityDaoSingleton.getInstance(requireContext())
        fetchLocationUseCase = FetchLocationUseCase(requireContext().applicationContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val startButton: Button = view.findViewById(R.id.startButton)
        startButton.setOnClickListener {
            RunnityWorker.enqueue(requireContext())
        }
        runnityDao.getAllLiveData().observe(viewLifecycleOwner, listenLocationUpdates)

        distanceTextView = view.findViewById(R.id.distanceTextView)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        updateMap()
    }

    private fun updateMap() {
        lifecycleScope.launch(Dispatchers.IO) {
            val location = fetchLocationUseCase()
            location?.let {
                withContext(Dispatchers.Main) {
                    val current = LatLng(location.latitude, location.longitude)
                    if (marker == null) {
                        marker = map?.addMarker(createMarker().position(current).title("Mi ubicacion"))
                    } else {
                        marker?.position = current
                    }
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(current, ZOOM_LEVEL))
                }
            }
        }
    }

    private fun createMarker(): MarkerOptions {
        val drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(
            ContextCompat.getDrawable(requireContext(), R.drawable.running)!!.toBitmap(), 85, 105, true)
        )
        return MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(drawable.toBitmap()))
    }

    private fun drawPrimaryLinePath(listLocsToDraw: List<LocationEntity>) {
        if (map == null) {
            return
        }
        if (listLocsToDraw.size < 2) {
            return
        }
        if (polyline == null) {
            polyline = map?.addPolyline(
                PolylineOptions().addAll(listLocsToDraw.map { LatLng(it.latitude, it.longitude) })
                    .color(ContextCompat.getColor(requireContext(), R.color.button_color))
                    .width(25F)
            )
        } else {
            polyline?.points = listLocsToDraw.map { LatLng(it.latitude, it.longitude) }
        }
        val lastLocation = listLocsToDraw.last()
        val lastPoint = LatLng(lastLocation.latitude, lastLocation.longitude)
        marker?.position = lastPoint
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, ZOOM_LEVEL))

        accumulateDistance(lastPoint.latitude, lastPoint.longitude)
    }

    private fun accumulateDistance(currentLatitude: Double, currentLongitude: Double){
        if (previousLatitude == 0.0 || previousLongitude == 0.0) {
            previousLatitude = currentLatitude
            previousLongitude = currentLongitude
            return
        }

        val previousLocation: Location = Location("previous")
        val currentLocation: Location = Location("current")

        previousLocation.latitude = previousLatitude
        previousLocation.longitude = previousLongitude

        currentLocation.latitude = currentLatitude
        currentLocation.longitude = currentLongitude

        distance += previousLocation.distanceTo(currentLocation)

        previousLatitude = currentLatitude
        previousLongitude = currentLongitude

        distanceTextView.text = String.format("%.02f", distance * M_TO_KM)
    }

    companion object {
        private const val ZOOM_LEVEL = 17f
        private const val M_TO_KM = 0.001
    }
}
