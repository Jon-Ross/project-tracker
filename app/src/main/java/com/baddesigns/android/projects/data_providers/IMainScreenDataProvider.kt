package com.baddesigns.android.projects.data_providers

import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel

/**
 * Created by Jon-Ross on 25/03/2018.
 */
interface IMainScreenDataProvider {

    fun fetchLists() : ListsDataModel
    fun fetchLists(callback: Callback)
    fun addProjectItems(vararg items: ListItemModel)
    fun addProjectItems(items: List<ListItemModel>)
    fun addLibraryItems(vararg items: ListItemModel)
    fun addLibraryItems(items: List<ListItemModel>)
    fun connectItems(item1: ListItemModel, item2: ListItemModel) : Boolean
    fun clearDb()

    interface Callback {
        fun onListsRetrieved(lists: ListsDataModel)
    }

}