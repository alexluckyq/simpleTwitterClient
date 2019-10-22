package bell.assignment.simpletwitterclient.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import bell.assignment.simpletwitterclient.R
import bell.assignment.simpletwitterclient.data.TweetData
import bell.assignment.simpletwitterclient.databinding.LayoutSearchListitemBinding
import bell.assignment.simpletwitterclient.utils.loadImageUrl
import kotlinx.android.synthetic.main.layout_profile.view.*

class SearchRecyclerViewAdapter(val onClickAction: ((TweetData) -> Unit)) :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {

    var tweetDataList = mutableListOf<TweetData>()

    fun updateTweetDataList(dataList: MutableList<TweetData>) {
        tweetDataList.clear()
        tweetDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val dataBinding = DataBindingUtil.inflate<LayoutSearchListitemBinding>(
            LayoutInflater.from(parent.context), R.layout.layout_search_listitem,
            parent, false)
        return SearchViewHolder(dataBinding)
    }

    override fun getItemCount(): Int = tweetDataList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindData(tweetDataList[position])
    }

    inner class SearchViewHolder(val binding: LayoutSearchListitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(tweetData: TweetData) {
            with(itemView) {
                binding.tweetData = tweetData
                profileImageView.loadImageUrl(
                    tweetData.profileImageUrl,
                    R.drawable.default_user_thumbnail
                )
                binding.executePendingBindings()

                binding.root.setOnClickListener {
                    binding.tweetData?.let {
                        onClickAction?.invoke(it)
                    }
                }
            }
        }
    }
}