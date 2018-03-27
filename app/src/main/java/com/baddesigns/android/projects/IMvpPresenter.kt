package com.baddesigns.android.projects

/**
 * Created by Jon-Ross on 25/03/2018.
 */
interface IMvpPresenter<in V : IMvpView> {
    fun setView(view: V)
}