package com.baddesigns.android.projects.models.view_models

/**
 * Created by Jon-Ross on 25/03/2018.
 */
data class MainScreenViewModel(
        val projectsList: List<ListItemViewModel> = mutableListOf(),
        val librariesList: List<ListItemViewModel> = mutableListOf())