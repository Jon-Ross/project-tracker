package com.baddesigns.android.projects

import com.baddesigns.android.projects.data_providers.IMainScreenDataProvider
import com.baddesigns.android.projects.mappers.MainScreenMapper
import com.baddesigns.android.projects.models.data_models.MainScreenModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class MainScreenPresenter(private val dataProvider: IMainScreenDataProvider,
                          private val mapper: MainScreenMapper) :
        MainScreenContract.Presenter, IMainScreenDataProvider.Callback {

    internal lateinit var view: MainScreenContract.View

    override fun start() {
        dataProvider.fetchLists(this)
    }

    override fun onListsRetrieved(lists: MainScreenModel) {
        val projectsListViewModels: List<ListItemViewModel> =
                mapper.mapDataToView(lists).projectsList
        val librariesListViewModels: List<ListItemViewModel> =
                mapper.mapDataToView(lists).librariesList

        view.updateProjectsListView(projectsListViewModels)
        view.updateLibrariesListView(librariesListViewModels)
    }

    override fun projectsHeaderArrowClicked(contentsWereShowing: Boolean) {
        val toShowing = !contentsWereShowing
        view.changeProjectsHeaderArrow(toShowing)
        view.changeProjectsListVisibility(toShowing)
    }

    override fun librariesHeaderArrowClicked(contentsWereShowing: Boolean) {
        val toShowing = !contentsWereShowing
        view.changeLibrariesHeaderArrow(toShowing)
        view.changeLibrariesListVisibility(toShowing)
    }

    override fun setView(view: MainScreenContract.View) {
        this.view = view
    }
}