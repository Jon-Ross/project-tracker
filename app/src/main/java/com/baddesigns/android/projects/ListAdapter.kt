package com.baddesigns.android.projects

import android.support.v7.widget.AppCompatCheckBox
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.baddesigns.android.projects.models.view_models.ListHeaderViewModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import com.bignerdranch.expandablerecyclerview.ChildViewHolder
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter
import com.bignerdranch.expandablerecyclerview.ParentViewHolder
import java.util.*


/**
 * Created by Jon-Ross on 25/03/2018.
 */
class ListAdapter(internal var items: MutableList<ListHeaderViewModel>) :
        ExpandableRecyclerAdapter<ListHeaderViewModel, ListItemViewModel,
                HeaderViewHolder, ListViewHolder>(items) {

    private var projectsListItemCheckboxListener: ListViewHolder.Callback? = null
    private var librariesListItemCheckboxListener: ListViewHolder.Callback? = null

    override fun onCreateParentViewHolder(parentViewGroup: ViewGroup, viewType: Int): HeaderViewHolder {
        val inflater = LayoutInflater.from(parentViewGroup.context)
        val view = inflater.inflate(R.layout.header_item, parentViewGroup, false)
        return HeaderViewHolder(view)
    }

    override fun onCreateChildViewHolder(childViewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(childViewGroup.context)
        val view = inflater.inflate(R.layout.list_item, childViewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindParentViewHolder(parentViewHolder: HeaderViewHolder,
                                        parentPosition: Int, parent: ListHeaderViewModel) {
        parentViewHolder.bindView(parent)
    }

    override fun onBindChildViewHolder(childViewHolder: ListViewHolder,
                                       parentPosition: Int, childPosition: Int, child: ListItemViewModel) {
        if(child.itemType == ItemType.PROJECT) {
            childViewHolder.setCallback(projectsListItemCheckboxListener)
        } else {
            childViewHolder.setCallback(librariesListItemCheckboxListener)
        }
        childViewHolder.bindView(child)
    }

    override fun setParentList(parentList: MutableList<ListHeaderViewModel>, preserveExpansionState: Boolean) {
        items = parentList
        super.setParentList(parentList, preserveExpansionState)
    }

    fun setProjectsListItems(list: List<ListItemViewModel>) {
        items[0].list = list as MutableList<ListItemViewModel>
        notifyParentDataSetChanged(true)
    }

    fun setLibrariesListItems(list: List<ListItemViewModel>) {
        items[1].list = list as MutableList<ListItemViewModel>
        notifyParentDataSetChanged(true)
    }

    fun setProjectsItemCheckboxListener(listener: ListViewHolder.Callback?) {
        projectsListItemCheckboxListener = listener
    }

    fun setLibrariesItemCheckboxListener(listener: ListViewHolder.Callback?) {
        librariesListItemCheckboxListener = listener
    }
}

class ListViewHolder(itemView: View) : ChildViewHolder<ListItemViewModel>(itemView) {

    internal var callback: Callback? = null

    private val itemName: TextView = itemView.findViewById(R.id.itemName)
    private val itemCheckBox: AppCompatCheckBox = itemView.findViewById(R.id.itemCheckBox)

    fun bindView(item: ListItemViewModel) {
        itemName.text = item.name
        itemCheckBox.setOnCheckedChangeListener(null)
        itemCheckBox.isChecked = item.selected
        itemCheckBox.setOnCheckedChangeListener {
            _ , checked ->
                item.selected = checked
                callback?.checkboxClicked(checked, item.id)
        }
        when(item.checkboxShowing) {
            true -> itemCheckBox.visibility = View.VISIBLE
            false -> itemCheckBox.visibility = View.GONE
        }
    }

    fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    interface Callback {
        fun checkboxClicked(checked: Boolean, id: UUID)
    }
}

class HeaderViewHolder(itemView: View) :
        ParentViewHolder<ListHeaderViewModel, ListItemViewModel>(itemView) {

    private val headerTitle: TextView = itemView.findViewById(R.id.headerTitle)
    private val headerArrow: ImageView = itemView.findViewById(R.id.headerArrow)

    fun bindView(header: ListHeaderViewModel) {
        headerTitle.text = header.title
    }

    override fun expandView() {
        animateExpand()
        super.expandView()
    }

    override fun collapseView() {
        animateCollapse()
        super.collapseView()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = ROTATION_DURATION
        rotate.fillAfter = true
        headerArrow.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = ROTATION_DURATION
        rotate.fillAfter = true
        headerArrow.animation = rotate
    }

    companion object {
        private const val ROTATION_DURATION: Long = 300
    }
}

enum class ItemType {
    PROJECT,
    LIBRARY
}