package com.android.test.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.android.test.R
import com.android.test.base.BaseRecyclerAdapter
import com.android.test.base.BaseViewHolder
import com.android.test.interfaces.OnRecyclerItemClickListener
import com.android.test.local_db.Category

class CategoryAdapter(context: Context):
    BaseRecyclerAdapter<Category, OnRecyclerItemClickListener<Category>, CategoryAdapter.CategoryVH>(context) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CategoryVH {
        return CategoryVH(inflate(R.layout.item_category_row, viewGroup))
    }

    class CategoryVH(itemView:View):
        BaseViewHolder<Category, OnRecyclerItemClickListener<Category>>(itemView) {

        var ivCategoryDp : ImageView
        var tvCategoryName : TextView
        var cardContent : CardView

        init {
            ivCategoryDp = itemView.findViewById(R.id.iv_category_dp)
            tvCategoryName = itemView.findViewById(R.id.tv_category_name)
            cardContent = itemView.findViewById(R.id.card_category_content)
        }

        override fun onBind(item: Category, listener: OnRecyclerItemClickListener<Category>?) {
            tvCategoryName.text = item.categoryName
            cardContent.setOnClickListener {
                listener?.onItemClickListener(item, true)
            }
        }

    }

}