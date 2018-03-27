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
class ListAdapter(internal var items: List<ListItemViewModel>) :
        RecyclerView.Adapter<ListViewHolder>(), ListViewHolder.Callback {

    internal lateinit var callback: Callback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setCallback(this)
        holder.bindView(items[position])
    }

    fun setAllCheckboxesVisibility(showing: Boolean) {
        items.forEach {
            it.checkboxShowing = showing
        }
        notifyDataSetChanged()
    }

    fun filterList(ids: List<UUID>) {
        for(item in items) {
            item.hidden = true
            for(id in ids) {
                if(item.id == id) {
                    item.hidden = false
                    break
                }
            }
        }
        notifyDataSetChanged()
    }

    fun removeFilter() {
        items.forEach {
            it.hidden = false
        }
        notifyDataSetChanged()
    }

    override fun checkboxClicked(checked: Boolean) {
        val count = countSelected()
        if(count <= 1 && (checked || count <= 0)) callback.setAllCheckboxesVisibility(!checked)

        val ids = retrieveSelectedIds()
        if(ids.isEmpty()) callback.removeFilter() else callback.filterList(ids)
    }

    fun retrieveSelectedIds() : List<UUID> {
        val ids = mutableListOf<UUID>()
        items.forEach {
            if(it.selected) ids.add(it.id)
        }
        return ids
    }

    private fun countSelected() : Int {
        var count = 0
        items.forEach {
            if(it.selected) count++
        }
        return count
    }

    fun updateList(list: List<ListItemViewModel>) {
        items = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun setAllCheckboxesVisibility(showing: Boolean)
        fun filterList(ids: List<UUID>)
        fun removeFilter()
    }
}

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal lateinit var callback: Callback

    private val itemName: TextView = itemView.findViewById(R.id.itemName)
    private val itemCheckBox: AppCompatCheckBox = itemView.findViewById(R.id.itemCheckBox)

    fun bindView(item: ListItemViewModel) {
        itemName.text = item.name
        itemCheckBox.isChecked = item.selected
        itemCheckBox.setOnCheckedChangeListener {
            _ , checked ->
            run {
                item.selected = checked
                callback.checkboxClicked(checked)
            }
        }
        when(item.checkboxShowing) {
            true -> itemCheckBox.visibility = View.VISIBLE
            false -> itemCheckBox.visibility = View.GONE
        }
        when(item.hidden) {
            true -> itemView.visibility = View.GONE
            false -> itemView.visibility = View.VISIBLE
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun checkboxClicked(checked: Boolean)
    }
}
