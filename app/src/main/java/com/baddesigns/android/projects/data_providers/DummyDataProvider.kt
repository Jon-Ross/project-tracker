package com.baddesigns.android.projects.data_providers

import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel

/**
 * Created by Jon-Ross on 25/03/2018.
 */
object DummyDataProvider : IMainScreenDataProvider {

    internal val model = ListsDataModel()

    override fun addProjectItems(vararg items: ListItemModel) {
        items.forEach {
            model.projectsList.add(it)
        }
    }

    override fun addProjectItems(items: List<ListItemModel>) {
        model.projectsList.addAll(items)
    }

    override fun addLibraryItems(vararg items: ListItemModel) {
        items.forEach {
            model.librariesList.add(it)
        }
    }

    override fun addLibraryItems(items: List<ListItemModel>) {
        model.librariesList.addAll(items)
    }

    override fun fetchLists() : ListsDataModel {
        return model
    }

    override fun fetchLists(callback: IMainScreenDataProvider.Callback) {
        callback.onListsRetrieved(model)
    }

    override fun connectItems(item1: ListItemModel, item2: ListItemModel) : Boolean {
        if((!model.projectsList.contains(item1) && !model.librariesList.contains(item1)) ||
           (!model.projectsList.contains(item2) && !model.librariesList.contains(item2)))
            return false
        if(model.projectsList.containsAll(listOf(item1, item2))) return false
        if(model.librariesList.containsAll(listOf(item1, item2))) return false

        connectAnyItems(item1, item2)
        return true
    }

    override fun clearDb() {
        model.projectsList.clear()
        model.librariesList.clear()
    }

    private fun connectAnyItems(item1: ListItemModel, item2: ListItemModel) {
        item1.connections.add(item2.id)
        item2.connections.add(item1.id)
    }
}