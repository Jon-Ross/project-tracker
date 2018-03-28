package com.baddesigns.android.projects

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import junit.framework.Assert.*
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
        assertNotNull(activity.projectsListView.adapter)

        assertNotNull(activity.librariesListView.layoutManager)
        assertNotNull(activity.librariesAdapter)
        assertNotNull(activity.librariesListView.adapter)

        assertTrue(activity.projectsHeaderArrow.hasOnClickListeners())
        assertTrue(activity.librariesHeaderArrow.hasOnClickListeners())

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
}