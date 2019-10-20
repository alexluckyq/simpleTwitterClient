package bell.assignment.simpletwitterclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bell.assignment.simpletwitterclient.R
import bell.assignment.simpletwitterclient.viewmodels.MapViewModel

class MapFragment: Fragment() {

    private lateinit var mapViewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        val rootView =
            inflater.inflate(R.layout.fragment_map, container, false)
        return rootView
    }
}