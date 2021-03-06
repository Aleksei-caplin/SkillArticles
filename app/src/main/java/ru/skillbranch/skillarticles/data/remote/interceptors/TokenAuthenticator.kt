package ru.skillbranch.skillarticles.data.remote.interceptors

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.skillbranch.skillarticles.data.local.PrefManager
import ru.skillbranch.skillarticles.data.remote.NetworkManager
import ru.skillbranch.skillarticles.data.remote.req.RefreshReq
import java.io.IOException

class TokenAuthenticator : Authenticator {
    /**
     * Authenticator for when the authToken need to be refresh and updated
     * every time we get a 401 error code
     */
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.code != 401) return null

        val api = NetworkManager.api
        val pref = PrefManager
        val refreshRes = api.refresh(RefreshReq(pref.refreshToken)).execute()

        if (!refreshRes.isSuccessful) return null

        val tokens = refreshRes.body()!!
        val access = NetworkManager.getAccessTokenWithType(tokens.accessToken)
        pref.accessToken = access
        pref.refreshToken = tokens.refreshToken

        return response.request.newBuilder()
            .header("Authorization", access)
            .build()
    }
}
