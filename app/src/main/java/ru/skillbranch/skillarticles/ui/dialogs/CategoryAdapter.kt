package ru.skillbranch.skillarticles.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.*
import kotlinx.android.synthetic.main.item_category.view.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.local.entities.CategoryData
import ru.skillbranch.skillarticles.extensions.dpToIntPx

class CategoryAdapter(private val listener: (CategoryData, Boolean) -> Unit) :
    ListAdapter<Pair<CategoryData, Boolean>, CategoryVH>(CategoryCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryVH(view)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val it = getItem(position)
        holder.bind(it.first, it.second, listener)
    }
}

class CategoryVH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    private val categorySize = containerView.context.dpToIntPx(40)

    fun bind(item: CategoryData, checked: Boolean, listener: (CategoryData, Boolean) -> Unit) {
        containerView.ch_select.isChecked = checked
        containerView.ch_select.setOnCheckedChangeListener { _, isChecked ->
            listener(item, isChecked)
        }

        Glide.with(containerView.context)
            .load(item.icon)
            .circleCrop()
            .override(categorySize)
            .into(containerView.iv_icon)

        containerView.tv_category.text = item.title
        containerView.tv_count.text = "${item.articlesCount}"

        itemView.setOnClickListener {
            containerView.ch_select.isChecked = !containerView.ch_select.isChecked
            //listener(item, containerView.ch_select.isChecked)
        }
    }
}

class CategoryCallback : DiffUtil.ItemCallback<Pair<CategoryData, Boolean>>() {
    override fun areItemsTheSame(
        oldItem: Pair<CategoryData, Boolean>,
        newItem: Pair<CategoryData, Boolean>
    ) =
        oldItem.first.categoryId == newItem.first.categoryId

    override fun areContentsTheSame(
        oldItem: Pair<CategoryData, Boolean>,
        newItem: Pair<CategoryData, Boolean>
    ) =
        oldItem == newItem
}