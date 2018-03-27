package com.baddesigns.android.projects

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import junit.framework.Assert.assertEquals
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
        activity.projectsAdapter = projectsAdapter
        activity.librariesAdapter = librariesAdapter
    }

    private fun testOnCreate() {
        assertNotNull(activity.upArrowDrawable)
        assertNotNull(activity.downArrowDrawable)

        // headers start expanded
        assertEquals(downArrowDrawableState, activity.projectsHeaderArrow.drawable.constantState)
        assertEquals(downArrowDrawableState, activity.librariesHeaderArrow.drawable.constantState)

        assertNotNull(activity.projectsListView.layoutManager)
        assertNotNull(activity.projectsAdapter)
        assertNotNull(activity.projectsAdapter.callback)
        assertNotNull(activity.projectsListView.adapter)

        assertNotNull(activity.librariesListView.layoutManager)
        assertNotNull(activity.librariesAdapter)
        assertNotNull(activity.librariesAdapter.callback)
        assertNotNull(activity.librariesListView.adapter)

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

        verify(projectsAdapter).updateList(viewModels)
    }

    @Test
    fun updateLibrariesListView() {
        val viewModels: List<ListItemViewModel> = mutableListOf()
        activity.updateLibrariesListView(viewModels)

        verify(librariesAdapter).updateList(viewModels)
    }

    @Test
    fun whenClickProjectsHeaderArrow_contentsWereShowing_delegateToPresenter() {
        activity.projectsHeaderArrow.setImageDrawable(activity.downArrowDrawable)

        activity.projectsHeaderArrow.performClick()

        verify(presenter).projectsHeaderArrowClicked(true)
    }

    @Test
    fun whenClickProjectsHeaderArrow_contentsWereNotShowing_delegateToPresenter() {
        activity.projectsHeaderArrow.setImageDrawable(activity.upArrowDrawable)

        activity.projectsHeaderArrow.performClick()

        verify(presenter).projectsHeaderArrowClicked(false)
    }

    @Test
    fun whenClickLibrariesHeaderArrow_contentsWereShowing_delegateToPresenter() {
        activity.librariesHeaderArrow.setImageDrawable(activity.downArrowDrawable)

        activity.librariesHeaderArrow.performClick()

        verify(presenter).librariesHeaderArrowClicked(true)
    }

    @Test
    fun whenClickLibrariesHeaderArrow_contentsWereNotShowing_delegateToPresenter() {
        activity.librariesHeaderArrow.setImageDrawable(activity.upArrowDrawable)

        activity.librariesHeaderArrow.performClick()

        verify(presenter).librariesHeaderArrowClicked(false)
    }

    @Test
    fun changeProjectsListVisibility_toVisible() {
        activity.changeProjectsListVisibility(true)

        assertEquals(View.VISIBLE, activity.projectsListView.visibility)
    }

    @Test
    fun changeProjectsListVisibility_toGone() {
        activity.changeProjectsListVisibility(false)

        assertEquals(View.GONE, activity.projectsListView.visibility)
    }

    @Test
    fun changeLibrariesListVisibility_toVisible() {
        activity.changeLibrariesListVisibility(true)

        assertEquals(View.VISIBLE, activity.librariesListView.visibility)
    }

    @Test
    fun changeLibrariesListVisibility_toGone() {
        activity.changeLibrariesListVisibility(false)

        assertEquals(View.GONE, activity.librariesListView.visibility)
    }

    @Test
    fun changeProjectsHeaderArrow_toUpArrow() {
        activity.changeProjectsHeaderArrow(false)

        assertEquals(upArrowDrawableState, activity.projectsHeaderArrow.drawable.constantState)
    }

    @Test
    fun changeProjectsHeaderArrow_toDownArrow() {
        activity.changeProjectsHeaderArrow(true)

        assertEquals(downArrowDrawableState, activity.projectsHeaderArrow.drawable.constantState)
    }

    @Test
    fun changeLibrariesHeaderArrow_toUpArrow() {
        activity.changeLibrariesHeaderArrow(false)

        assertEquals(upArrowDrawableState, activity.librariesHeaderArrow.drawable.constantState)
    }

    @Test
    fun changeLibrariesHeaderArrow_toDownArrow() {
        activity.changeLibrariesHeaderArrow(true)

        assertEquals(downArrowDrawableState, activity.librariesHeaderArrow.drawable.constantState)
    }

    @Test
    fun projectsListCheckboxListenerSetAllCheckboxesVisibility_true() {
        activity.projectsListCheckboxListener.setAllCheckboxesVisibility(true)

        verify(librariesAdapter).setAllCheckboxesVisibility(true)
    }

    @Test
    fun projectsListCheckboxListenerSetAllCheckboxesVisibility_false() {
        activity.projectsListCheckboxListener.setAllCheckboxesVisibility(false)

        verify(librariesAdapter).setAllCheckboxesVisibility(false)
    }

    @Test
    fun projectsListCheckboxListenerFilterList() {
        val filteredList = listOf<UUID>(UUID.randomUUID(), UUID.randomUUID())

        activity.projectsListCheckboxListener.filterList(filteredList)

        verify(librariesAdapter).filterList(filteredList)
    }

    @Test
    fun projectsListCheckboxListenerRemoveFilter() {
        activity.projectsListCheckboxListener.removeFilter()

        verify(librariesAdapter).removeFilter()
    }

    @Test
    fun librariesListCheckboxListenerSetAllCheckboxesVisibility_true() {
        activity.librariesListCheckboxListener.setAllCheckboxesVisibility(true)

        verify(projectsAdapter).setAllCheckboxesVisibility(true)
    }

    @Test
    fun librariesListCheckboxListenerSetAllCheckboxesVisibility_false() {
        activity.librariesListCheckboxListener.setAllCheckboxesVisibility(false)

        verify(projectsAdapter).setAllCheckboxesVisibility(false)
    }

    @Test
    fun librariesListCheckboxListenerFilterList() {
        val filteredList = listOf<UUID>(UUID.randomUUID(), UUID.randomUUID())

        activity.librariesListCheckboxListener.filterList(filteredList)

        verify(projectsAdapter).filterList(filteredList)
    }

    @Test
    fun librariesListCheckboxListenerRemoveFilter() {
        activity.librariesListCheckboxListener.removeFilter()

        verify(projectsAdapter).removeFilter()
    }
}