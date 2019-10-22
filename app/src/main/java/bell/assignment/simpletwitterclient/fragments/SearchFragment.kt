package bell.assignment.simpletwitterclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import bell.assignment.simpletwitterclient.activities.DetailActivity
import bell.assignment.simpletwitterclient.adapters.SearchRecyclerViewAdapter
import bell.assignment.simpletwitterclient.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*


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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchAdapter = SearchRecyclerViewAdapter {
            DetailActivity.openActivity(it.tweetId, context = context)
        }
        searchRecyclerView.adapter = searchAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText.isNullOrEmpty()) {
                    searchViewModel.init()
                } else {
                    searchViewModel.retrieveTweetsByKeywords(
                        query = newText
                    )
                }
                return true
            }
        })

        searchViewModel.getTweetDataObservable().observe(this, Observer {
            it?.let {
                if (it.isEmpty()) {
                    noResultTextView.visibility = View.VISIBLE
                } else {
                    noResultTextView.visibility = View.GONE
                    searchAdapter.updateTweetDataList(it.toMutableList())
                }
            }
        })

//        searchViewModel.init()
    }

}