package com.utnfrbamobile.runnity.ui

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.utnfrbamobile.runnity.R


class MapFragment : Fragment(), OnMapReadyCallback {

    private var map : GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    private fun drawPrimaryLinePath(listLocsToDraw: List<Location>) {
        if (map == null) {
            return
        }
        if (listLocsToDraw.size < 2) {
            return
        }
        val options = PolylineOptions()
        options.color(Color.parseColor("#CC0000FF"))
        options.width(5f)
        options.visible(true)
        for (locRecorded in listLocsToDraw) {
            options.add(
                LatLng(
                    locRecorded.latitude,
                    locRecorded.longitude
                )
            )
        }
        map!!.addPolyline(options)
    }
}
