package ru.skillbranch.skillarticles.viewmodels.base

import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*

abstract class BaseViewModel<T: IViewModelState>(initState:T): ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    val notifications = MutableLiveData<Event<Notify>>()

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
    protected fun notify(content: Notify) {
        notifications.value = Event(content)
    }

    //более компактная запись
    fun observeState(owner: LifecycleOwner, onChanged: (newState: T) -> Unit ) {
        state.observe(owner, Observer { onChanged(it!!) })
    }

    /***
     * более компактная форма записи observe() метода LiveData вызывает лямбда выражение обработчик
     * только в том случае если уведомление не было уже обработанно ранее,
     * реализует данное поведение с помощью EventObserver
     */
    fun observeNotifications(owner: LifecycleOwner, onNotify: (notification: Notify) -> Unit) {
        notifications.observe(owner, EventObserver{ onNotify(it) })
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

    fun saveState(outState: Bundle) {
        currentState.save(outState)
    }

    @Suppress("UNCHECKED_CAST")
    fun restoreState(savedState: Bundle) {
        state.value = currentState.restore(savedState) as T
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

    fun peekContent(): E = content
}

class EventObserver<E>(private val onEventUnHandledContent: (E) -> Unit): Observer<Event<E>> {
    override fun onChanged(event: Event<E>?) {
        event?.getContentIfNotHandled()?.let{
            onEventUnHandledContent(it)
        }
    }
}

sealed class Notify() {
    abstract val message: String
    data class TextMessage(override val message: String) : Notify( )

    data class ActionMessage(
        override val message: String,
        val actionLabel: String,
        val actionHandler: (() -> Unit)
    ) : Notify()

    data class ErrorMessage(
        override val message: String,
        val errLabel: String?,
        val errHandler: (() -> Unit)?
    ) : Notify()
}