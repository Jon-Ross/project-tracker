package com.baddesigns.android.projects.models.view_models

import com.bignerdranch.expandablerecyclerview.model.Parent

class ListHeaderViewModel(val title: String, var list: MutableList<ListItemViewModel>) :
        Parent<ListItemViewModel> {

    override fun getChildList(): MutableList<ListItemViewModel> = list

    override fun isInitiallyExpanded(): Boolean = true
}