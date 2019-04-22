package ru.belyaev.vitaliy.ott.presentation

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.belyaev.vitaliy.ott.entity.Company
import ru.belyaev.vitaliy.ott.entity.Order
import ru.belyaev.vitaliy.ott.entity.ToursData

interface MainView : MvpView {
    fun showProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showTours(show: Boolean, toursData: ToursData)
    fun showFlightsDialog(show: Boolean, order: Order?)
    fun showFilterDialog(show: Boolean, companies:List<Company>?)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}