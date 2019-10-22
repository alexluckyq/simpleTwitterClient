package bell.assignment.simpletwitterclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import bell.assignment.simpletwitterclient.adapters.SearchRecyclerViewAdapter
import bell.assignment.simpletwitterclient.viewmodels.SearchViewModel


class SearchFragment: Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchAdapter: SearchRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        val view = inflater.inflate(bell.assignment.simpletwitterclient.R.layout.fragment_search, container, false)
        return view
    }


}