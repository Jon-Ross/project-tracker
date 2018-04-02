package com.baddesigns.android.projects

import android.content.Intent
import android.graphics.drawable.Drawable
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import junit.framework.Assert.assertNotNull
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class MainActivityTest {

    @Mock
    private lateinit var presenter: MainScreenContract.Presenter
    @Mock
    private lateinit var projectsAdapter: ListAdapter
    @Mock
    private lateinit var librariesAdapter: ListAdapter

    private lateinit var activity: MainActivity
    private lateinit var activityController: ActivityController<MainActivity>

    private lateinit var upArrowDrawableState: Drawable.ConstantState
    private lateinit var downArrowDrawableState: Drawable.ConstantState

    @Before
    fun setUp() {
        initMocks(this)

        val intent = Intent(RuntimeEnvironment.application, MainActivity::class.java)
        activityController = Robolectric.buildActivity(MainActivity::class.java, intent)
        activityController.create()
        activity = activityController.get()

        upArrowDrawableState = activity.upArrowDrawable!!.constantState
        downArrowDrawableState = activity.downArrowDrawable!!.constantState

        testOnCreate()

        // Setting mocks
        activity.presenter = presenter
        activity.adapter = projectsAdapter
        activity.librariesAdapter = librariesAdapter
    }

    private fun testOnCreate() {
        assertNotNull(activity.upArrowDrawable)
        assertNotNull(activity.downArrowDrawable)

        // headers start expanded

        assertNotNull(activity.listsView.layoutManager)
        assertNotNull(activity.adapter)
        assertNotNull(activity.listsView.adapter)

        assertNotNull(activity.presenter)
    }

    @Test
    fun onStart() {
        activityController.start()
        activity = activityController.get()

        verify(presenter).start()
    }

    @Test
    fun updateProjectsListView() {
        val viewModels: List<ListItemViewModel> = mutableListOf()

        activity.updateProjectsListView(viewModels)

        verify(projectsAdapter).setListItems(viewModels)
    }

    @Test
    fun updateLibrariesListView() {
        val viewModels: List<ListItemViewModel> = mutableListOf()

        activity.updateLibrariesListView(viewModels)

        verify(librariesAdapter).setListItems(viewModels)
    }

    @Test
    fun projectsListItemCheckboxListener_checkboxClicked_delegateToPresenter() {
        val checked = true
        val id = UUID.randomUUID()

        activity.projectsListItemCheckboxCallback.checkboxClicked(checked, id)

        verify(presenter).projectsListItemCheckboxClicked(checked, id)
    }

    @Test
    fun librariesListItemCheckboxListener_checkboxClicked_delegateToPresenter() {
        val checked = true
        val id = UUID.randomUUID()

        activity.librariesListItemCheckboxCallback.checkboxClicked(checked, id)

        verify(presenter).librariesListItemCheckboxClicked(checked, id)
    }

    @Test
    fun getProjectsList() {
        activity.getProjectsList()

        verify(projectsAdapter).getListItems()
    }

    @Test
    fun getLibrariesList() {
        activity.getLibrariesList()

        verify(librariesAdapter).getListItems()
    }
}