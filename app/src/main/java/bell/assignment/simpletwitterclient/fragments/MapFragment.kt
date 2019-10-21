package bell.assignment.simpletwitterclient.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bell.assignment.simpletwitterclient.R
import bell.assignment.simpletwitterclient.adapters.PinAdapter
import bell.assignment.simpletwitterclient.managers.cache.CacheManager
import bell.assignment.simpletwitterclient.utils.getLatLng
import bell.assignment.simpletwitterclient.utils.getLatLngFromTweet
import bell.assignment.simpletwitterclient.utils.getTweetMarkerString
import bell.assignment.simpletwitterclient.utils.isLocationPermissionGranted
import bell.assignment.simpletwitterclient.viewmodels.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel
    private lateinit var googleMap: GoogleMap
    private var isMapReady = false
    private var firstTimeSelect = false


    val radiusNames = arrayOf("5 KM", "10 KM", "20 KM", "25 KM", "50 KM", "100 KM", "150 KM", "200 KM")
    val radiusNumbers = arrayListOf(5, 10, 20, 25, 50, 100, 150, 200)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        val rootView =
            inflater.inflate(bell.assignment.simpletwitterclient.R.layout.fragment_map, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapViewModel.getTweetsObservable().observe(this, Observer {
            it?.let {
                addTweetPins()
            }
        })
        mapViewModel.fetchTweetsByLocation()
        val fragmentManager = activity?.supportFragmentManager
        val mapFragment = SupportMapFragment.newInstance()
        fragmentManager?.beginTransaction()
            ?.replace(bell.assignment.simpletwitterclient.R.id.mapFragment, mapFragment)?.commit()
        mapFragment?.getMapAsync(this)
        val spinnerAdapter = ArrayAdapter<String>(context, R.layout.spinner_center_item, radiusNames)
        radiusSpinner.adapter = spinnerAdapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.isMapReady = true

        val currentLocation = CacheManager.getCachedLocation().getLatLng()
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        activity?.let {
            val adapter = PinAdapter(it)
            googleMap.setInfoWindowAdapter(adapter)
            googleMap.setOnInfoWindowClickListener(adapter)
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentLocation,
                    getZoomLevel()
                )
            )
            isLocationPermissionGranted(it)?.let {
                googleMap.isMyLocationEnabled = true
            }
        }

        handleMapReady()
        addTweetPins()
    }


    fun addTweetPins() {
        if (mapViewModel.getTweetsObservable().value.isNullOrEmpty() || !isMapReady) {
            return
        }

        val tweets = mapViewModel.getTweetsObservable().value
        googleMap.clear()

        tweets?.map { Pair(it, it.getLatLngFromTweet()) }
            ?.filter {it2 -> it2.second != null }
            ?.forEach {
                val markerSnippetString = it.first.getTweetMarkerString()
                googleMap.addMarker(
                    it.second?.let { latlng ->
                        MarkerOptions()
                            .position(latlng)
                            .title(it.first.user.name)
                            .snippet(markerSnippetString)
                    }
                )
            }
    }

    fun handleMapReady() {
        val currentLocation = CacheManager.getCachedLocation().getLatLng()
        radiusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Radius Spinner", "Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (!firstTimeSelect) {
                    firstTimeSelect = true
                    return
                }
                CacheManager.setRadiusCache((radiusNumbers[position]))
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLocation,
                        getZoomLevel()
                    )
                )
                mapViewModel.fetchTweetsByLocation()
            }
        }
    }

    fun getZoomLevel(): Float {
        var zoomLevel = 11
        val radiusCircle = CacheManager.getCachedRadius().toDouble() * 1000
        val radius = radiusCircle + radiusCircle / 2
        val scale = radius / 500
        zoomLevel = (16 - Math.log(scale) / Math.log(2.0)).toInt()
        return zoomLevel + 0.4F
    }

}