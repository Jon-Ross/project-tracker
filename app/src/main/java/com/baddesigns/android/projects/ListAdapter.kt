package com.baddesigns.android.projects

import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class ListAdapter(internal var items: List<ListItemViewModel>,
                  private val listItemCheckboxListener: ListViewHolder.Callback) :
        RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setCallback(listItemCheckboxListener)
        holder.bindView(items[position])
    }

    fun getListItems() : List<ListItemViewModel> {
        return items
    }

    fun setListItems(list: List<ListItemViewModel>) {
        items = list
        notifyDataSetChanged()
    }
}

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal lateinit var callback: Callback

    private val itemName: TextView = itemView.findViewById(R.id.itemName)
    private val itemCheckBox: AppCompatCheckBox = itemView.findViewById(R.id.itemCheckBox)

    fun bindView(item: ListItemViewModel) {
        itemName.text = item.name
        itemCheckBox.setOnCheckedChangeListener(null)
        itemCheckBox.isChecked = item.selected
        itemCheckBox.setOnCheckedChangeListener {
            _ , checked ->
                item.selected = checked
                callback.checkboxClicked(checked, item.id)
        }
        when(item.checkboxShowing) {
            true -> itemCheckBox.visibility = View.VISIBLE
            false -> itemCheckBox.visibility = View.GONE
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun checkboxClicked(checked: Boolean, id: UUID)
    }
}
