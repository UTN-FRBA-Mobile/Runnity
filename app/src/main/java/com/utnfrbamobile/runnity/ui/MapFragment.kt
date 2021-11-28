package com.utnfrbamobile.runnity.ui


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.data.LocationEntity
import com.utnfrbamobile.runnity.data.RunnityDao
import com.utnfrbamobile.runnity.data.RunnityDaoSingleton
import com.utnfrbamobile.runnity.domain.FetchLocationUseCase
import com.utnfrbamobile.runnity.util.onBackPressedCustomAction
import com.utnfrbamobile.runnity.work.RunnityWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapFragment : Fragment(), OnMapReadyCallback {

    private var map : GoogleMap? = null

    private lateinit var runnutyDao: RunnityDao
    private lateinit var fetchLocationUseCase : FetchLocationUseCase
    private var marker: Marker? = null
    private var polyline: Polyline? = null

    private val listenLocationUpdates = Observer { newLocations: List<LocationEntity> ->
        drawPrimaryLinePath(newLocations)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    private fun bind() = context?.let {
        runnutyDao = RunnityDaoSingleton.getInstance(requireContext())
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
        onBackPressedCustomAction {
            findNavController().navigate(R.id.action_mapFragment_to_competitionDetailFragment)
        }
        runnutyDao.getAllLiveData().observe(viewLifecycleOwner, listenLocationUpdates)
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
    }

    companion object {
        private const val ZOOM_LEVEL = 17f
    }
}
