package ru.skillbranch.skillarticles.viewmodels

class ArticleViewModel(articleIs: String): BaseViewModel<ArticleState>(ArticleState()) {


    init{

    }
    fun handleUpText() {

    }

    fun handleDownText() {

    }

    fun handleNightMode() {

    }

    fun handleLike() {

    }

    fun handleBookmark() {

    }

    fun handleShare() {

    }

    fun handleToggleMenu() {
        updateState { it.copy(isShowMenu = !it.isShowMenu) }
    }
}

data class ArticleState(
    val isAuth: Boolean = false, // пользователь авторизован
    val isLoadingContent: Boolean = true,
    val isLoadingReviews: Boolean = true,
    val isLike: Boolean = false,
    val isBookmark: Boolean = false,
    val isShowMenu: Boolean = false,
    val isBigText: Boolean = false,
    val isDarkMode: Boolean = false,
    val isSearch: Boolean = false,
    val searchQuery: String? = null,
    val searchResults: List<Pair<Int, Int>> = emptyList(), // результат поиска стартовая и конечная позиция
    val searchPosition: Int = 0,
    val shareLink: String? = null,
    val title: String? = null,
    val category: String? = null,
    val categoryIconval : Any? = null,
    val date: String? = null,
    val author: Any? = null,
    val poster: String? = null,
    val content: List<Any> = emptyList(),
    val reviews: List<Any> = emptyList()
)