package com.example.studentnest.ui.listings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentnest.data.Listing
import com.example.studentnest.databinding.ItemListingBinding

class ListingAdapter(
    private val onClick: (Listing) -> Unit
) : ListAdapter<Listing, ListingAdapter.ListingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val binding = ItemListingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    class ListingViewHolder(private val binding: ItemListingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listing: Listing, onClick: (Listing) -> Unit) {
            binding.tvTitle.text = listing.title
            binding.tvPrice.text = "BWP ${listing.priceBWP} / month"
            binding.tvLocation.text = listing.location
            binding.tvStatus.text = listing.status
            binding.ivPhoto.setImageResource(listing.imageResId)
            binding.root.setOnClickListener { onClick(listing) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Listing>() {
            override fun areItemsTheSame(oldItem: Listing, newItem: Listing) =
                oldItem.listingId == newItem.listingId

            override fun areContentsTheSame(oldItem: Listing, newItem: Listing) =
                oldItem == newItem
        }
    }
}
