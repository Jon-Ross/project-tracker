package com.baddesigns.android.projects

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
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

    internal lateinit var projectsAdapter: ListAdapter
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
        initListsVisibilities()
        initPresenter()
        initArrowClickListeners()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun updateProjectsListView(projectsListViewModel: List<ListItemViewModel>) {
        projectsAdapter.setListItems(projectsListViewModel)
    }

    override fun updateLibrariesListView(librariesListViewModel: List<ListItemViewModel>) {
        librariesAdapter.setListItems(librariesListViewModel)
    }

    override fun changeProjectsListVisibility(visible: Boolean) {
        val listVisibleState = if(visible) View.VISIBLE else View.GONE
        projectsListView.visibility = listVisibleState
    }

    override fun changeLibrariesListVisibility(visible: Boolean) {
        when(visible) {
            true -> librariesListView.visibility = View.VISIBLE
            false -> librariesListView.visibility = View.GONE
        }
    }

    override fun changeProjectsHeaderArrow(downArrow: Boolean) {
        val arrowDrawable = if(downArrow) downArrowDrawable else upArrowDrawable
        projectsHeaderArrow.setImageDrawable(arrowDrawable)
    }

    override fun changeLibrariesHeaderArrow(downArrow: Boolean) {
        val arrowDrawable = if(downArrow) downArrowDrawable else upArrowDrawable
        librariesHeaderArrow.setImageDrawable(arrowDrawable)
    }

    override fun getProjectsList() : List<ListItemViewModel> {
        return projectsAdapter.getListItems()
    }

    override fun getLibrariesList() : List<ListItemViewModel> {
        return librariesAdapter.getListItems()
    }

    private fun setProjectsListVisibility(showing: Boolean) {
        changeProjectsHeaderArrow(showing)
        changeProjectsListVisibility(showing)
    }

    private fun setLibrariesListVisibility(showing: Boolean) {
        changeLibrariesHeaderArrow(showing)
        changeLibrariesListVisibility(showing)
    }

    private fun initListsViews() {
        projectsListView.layoutManager = LinearLayoutManager(this)
        projectsAdapter = ListAdapter(mutableListOf(), projectsListItemCheckboxCallback)
        projectsListView.adapter = projectsAdapter

        librariesListView.layoutManager = LinearLayoutManager(this)
        librariesAdapter = ListAdapter(mutableListOf(), librariesListItemCheckboxCallback)
        librariesListView.adapter = librariesAdapter
    }

    private fun initListsVisibilities() {
        upArrowDrawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_drop_up)
        downArrowDrawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_drop_down)

        setProjectsListVisibility(true)
        setLibrariesListVisibility(true)
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

    private fun initArrowClickListeners() {
        projectsHeaderArrow.setOnClickListener {
            presenter.projectsHeaderArrowClicked(
                    // needed to use constantState as drawable isn't uniquely observable in test,
                    // constantState is
                    projectsHeaderArrow.drawable.constantState ==
                            downArrowDrawable!!.constantState)
        }
        librariesHeaderArrow.setOnClickListener {
            presenter.librariesHeaderArrowClicked(
                    // needed to use constantState as drawable isn't uniquely observable in test,
                    // constantState is
                    librariesHeaderArrow.drawable.constantState ==
                            downArrowDrawable!!.constantState
            )
        }
    }
}
