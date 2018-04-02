package com.baddesigns.android.projects

import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import java.util.*

/**
 * Created by Jon-Ross on 26/03/2018.
 */
interface MainScreenContract {

    interface View : IMvpView {
        fun updateProjectsListView(projectsListViewModel: List<ListItemViewModel>)
        fun updateLibrariesListView(librariesListViewModel: List<ListItemViewModel>)
        fun getProjectsList(): List<ListItemViewModel>
        fun getLibrariesList(): List<ListItemViewModel>
    }

    interface Presenter :IMvpPresenter<View> {
        fun start()
        fun projectsListItemCheckboxClicked(checked: Boolean, id: UUID)
        fun librariesListItemCheckboxClicked(checked: Boolean, id: UUID)
    }
}