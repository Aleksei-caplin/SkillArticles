package ru.skillbranch.skillarticles.viewmodels

import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*

abstract class BaseViewModel<T>(initState:T): ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    val notification = MutableLiveData<Event<Notify>>()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    val state: MediatorLiveData<T> = MediatorLiveData<T>().apply {
        value = initState
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    val currentState
        get() = state.value!!

    @UiThread
    protected inline fun updateState(update:(currentState:T) -> T) {
        val updatedState: T = update(currentState)
        state.value = updatedState
    }

    @UiThread
    protected fun notify(content:Notify) {
        notification.value = Event(content)
    }

    //более компактная запись
    fun observeState(owner: LifecycleOwner, onChanged: (newState: T) -> Unit ) {
        state.observe(owner, Observer { onChanged(it!!) })
    }


    fun observeNotifications(owner: LifecycleOwner, onNotify: (notification: Notify) -> Unit) {
        notification.observe(owner, EventObserver{ onNotify(it) })
    }

    protected fun <S> subscribeOnDataSource(
        source: LiveData<S>,
        onChanged: (newValue: S, currentState:T) -> T?
    ){
        state.addSource(source) {

            state.value = onChanged(it, currentState) ?: return@addSource
            Log.d("M_source",  state.value.toString() )
        }
    }
}

class ViewModelFactory(private val params: String): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(params) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class Event<out E>(private val content:E) {
    var hasBeenHandled = false

    // возвращает контент,который еще не был обработан или null
    fun getContentIfNotHandled():E? {
        return if(hasBeenHandled) null
        else{
            hasBeenHandled = true
            content
        }
    }
}

class EventObserver<E>(private val onEventUnHandledContent: (E) -> Unit): Observer<Event<E>> {
    override fun onChanged(event: Event<E>?) {
        event?.getContentIfNotHandled()?.let{
            onEventUnHandledContent(it)
        }
    }
}

sealed class Notify(val message:String) {
    data class TextMessage(val msg:String): Notify(msg)

    data class ActionMessage(
        val msg: String,
        val actionLabel: String,
        val actionHandler:(() -> Unit)?
    ): Notify(msg)

    data class ErrorMessage(
        val msg: String,
        val errLabel: String,
        val errHandler:(() -> Unit)?
    ): Notify(msg)


}