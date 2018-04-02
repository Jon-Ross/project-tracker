package com.baddesigns.android.projects

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.baddesigns.android.projects.data_providers.DummyDataProvider
import com.baddesigns.android.projects.generaters.connectAnyDataModels
import com.baddesigns.android.projects.generaters.generateLibrariesDataModelList
import com.baddesigns.android.projects.generaters.generateProjectsDataModelList
import com.baddesigns.android.projects.mappers.MainScreenMapper
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), MainScreenContract.View {

    internal lateinit var presenter: MainScreenContract.Presenter

    internal lateinit var adapter: ListAdapter
    internal lateinit var librariesAdapter: ListAdapter

    internal var upArrowDrawable: Drawable? = null
    internal var downArrowDrawable: Drawable? = null

    internal val projectsListItemCheckboxCallback: ListViewHolder.Callback =
            object : ListViewHolder.Callback {

        override fun checkboxClicked(checked: Boolean, id: UUID) {
            presenter.projectsListItemCheckboxClicked(checked, id)
        }
    }

    internal val librariesListItemCheckboxCallback: ListViewHolder.Callback =
            object : ListViewHolder.Callback {

        override fun checkboxClicked(checked: Boolean, id: UUID) {
            presenter.librariesListItemCheckboxClicked(checked, id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListsViews()
        initPresenter()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun updateProjectsListView(projectsListViewModel: List<ListItemViewModel>) {
        adapter.setListItems(projectsListViewModel)
    }

    override fun updateLibrariesListView(librariesListViewModel: List<ListItemViewModel>) {
        librariesAdapter.setListItems(librariesListViewModel)
    }

    override fun getProjectsList() : List<ListItemViewModel> {
        return adapter.getListItems()
    }

    override fun getLibrariesList() : List<ListItemViewModel> {
        return librariesAdapter.getListItems()
    }

    private fun initListsViews() {
        listsView.layoutManager = LinearLayoutManager(this)
        adapter = ListAdapter(mutableListOf(), projectsListItemCheckboxCallback)
        listsView.adapter = adapter
    }

    private fun initPresenter() {
        val projects = generateProjectsDataModelList()
        val libraries = generateLibrariesDataModelList()
        connectAnyDataModels(projects[0], libraries[5])
        connectAnyDataModels(projects[0], libraries[6])
        connectAnyDataModels(projects[1], libraries[2])
        connectAnyDataModels(projects[1], libraries[3])
        connectAnyDataModels(projects[1], libraries[5])
        connectAnyDataModels(projects[2], libraries[0])

        val dataProvider = DummyDataProvider
        dataProvider.addLibraryItems(libraries)
        dataProvider.addProjectItems(projects)

        presenter = MainScreenPresenter(dataProvider, MainScreenMapper())
        presenter.setView(this)
    }
}
