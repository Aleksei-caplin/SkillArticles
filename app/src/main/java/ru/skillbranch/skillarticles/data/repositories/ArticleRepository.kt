package ru.skillbranch.skillarticles.data.repositories

import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.*

object ArticleRepository {

    private val local = LocalDataHolder
    private val network = NetworkDataHolder

    fun loadArticleContent(articleId:String): LiveData<String?> {
        return network.loadArticleContent(articleId)  // 5s задержка из сети
    }

    fun getArticle(articleId: String): LiveData<ArticleData?> {
        return local.findArticle(articleId) // 2s задержка из базы
    }

    fun loadArticlePersonalInfo(articleId: String): LiveData<ArticlePersonalInfo?> {
        return local.findArticlePersonalInfo(articleId) //  1c задержка из базы
    }

    fun getAppSettings(): LiveData<AppSettings> = local.getAppSettings() // из preferences

    fun updateSettings(appSettings:AppSettings) {
        local.updateAppSettings(appSettings)
    }

    fun updateArticlePersonalInfo(info:ArticlePersonalInfo) {
        local.updateArticlePersonalInfo(info)
    }
}