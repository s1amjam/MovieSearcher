package com.moviesearcher.api

import androidx.lifecycle.MutableLiveData
import com.moviesearcher.common.model.account.AccountResponse
import com.moviesearcher.common.model.auth.CreateSessionResponse
import com.moviesearcher.common.model.auth.CreateTokenResponse
import com.moviesearcher.common.model.auth.DeleteSessionResponse
import com.moviesearcher.common.model.auth.RequestToken
import com.moviesearcher.common.model.auth.SessionId
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.common.model.common.ResponseWithCodeAndMessage
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.CheckItemStatusResponse
import com.moviesearcher.list.model.CreateNewList
import com.moviesearcher.list.model.CreateNewListResponse
import com.moviesearcher.list.model.ListResponse
import com.moviesearcher.list.model.add.AddToListResponse
import com.moviesearcher.person.model.combinedcredits.CombinedCreditsResponse
import com.moviesearcher.person.model.images.PersonImagesResponse
import com.moviesearcher.person.model.person.PersonResponse
import com.moviesearcher.rated.movie.model.RatedMoviesResponse
import com.moviesearcher.rated.tv.model.RatedTvsResponse
import com.moviesearcher.rated.tvepisode.model.RatedTvEpisodesResponse
import com.moviesearcher.search.model.SearchResponse
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "Api"

object Api {

    fun search(query: String): MutableLiveData<SearchResponse> {
        val responseLiveData: MutableLiveData<SearchResponse> = MutableLiveData()
        val resp = ApiService.create().search(query)

        resp.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<SearchResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun createRequestToken(): MutableLiveData<CreateTokenResponse> {
        val resp = ApiService.create().newRequestToken()
        val responseLiveData: MutableLiveData<CreateTokenResponse> = MutableLiveData()

        resp.enqueue(object : Callback<CreateTokenResponse> {
            override fun onResponse(
                call: Call<CreateTokenResponse>,
                response: Response<CreateTokenResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CreateTokenResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun createSession(requestToken: RequestToken): MutableLiveData<CreateSessionResponse> {
        val resp = ApiService.create().createSession(requestToken = requestToken)
        val responseLiveData: MutableLiveData<CreateSessionResponse> = MutableLiveData()

        resp.enqueue(object : Callback<CreateSessionResponse> {
            override fun onResponse(
                call: Call<CreateSessionResponse>,
                response: Response<CreateSessionResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CreateSessionResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun deleteSession(sessionId: SessionId): MutableLiveData<DeleteSessionResponse> {
        val resp = ApiService.create().deleteSession(sessionId = sessionId)
        val responseLiveData: MutableLiveData<DeleteSessionResponse> = MutableLiveData()

        resp.enqueue(object : Callback<DeleteSessionResponse> {
            override fun onResponse(
                call: Call<DeleteSessionResponse>,
                response: Response<DeleteSessionResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<DeleteSessionResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getAccount(sessionId: String?): MutableLiveData<AccountResponse> {
        val responseLiveData: MutableLiveData<AccountResponse> = MutableLiveData()
        val resp = ApiService.create().getAccount(sessionId!!)

        resp.enqueue(object : Callback<AccountResponse> {
            override fun onResponse(
                call: Call<AccountResponse>,
                response: Response<AccountResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<AccountResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getLists(accountId: Long?, sessionId: String?, page: Int): MutableLiveData<ListsResponse> {
        val responseLiveData: MutableLiveData<ListsResponse> = MutableLiveData()
        val resp = ApiService.create().getCreatedLists(accountId, sessionId, page)

        resp.enqueue(object : Callback<ListsResponse> {
            override fun onResponse(
                call: Call<ListsResponse>,
                response: Response<ListsResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ListsResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getRatedMovies(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<RatedMoviesResponse> {
        val responseLiveData: MutableLiveData<RatedMoviesResponse> = MutableLiveData()
        val resp = ApiService.create().getRatedMovies(accountId, sessionId)

        resp.enqueue(object : Callback<RatedMoviesResponse> {
            override fun onResponse(
                call: Call<RatedMoviesResponse>,
                response: Response<RatedMoviesResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<RatedMoviesResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getRatedTvs(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<RatedTvsResponse> {
        val responseLiveData: MutableLiveData<RatedTvsResponse> = MutableLiveData()
        val resp = ApiService.create().getRatedTvs(accountId, sessionId)

        resp.enqueue(object : Callback<RatedTvsResponse> {
            override fun onResponse(
                call: Call<RatedTvsResponse>,
                response: Response<RatedTvsResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<RatedTvsResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getRatedTvEpisodes(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<RatedTvEpisodesResponse> {
        val responseLiveData: MutableLiveData<RatedTvEpisodesResponse> = MutableLiveData()
        val resp = ApiService.create().getRatedTvEpisodes(accountId, sessionId)

        resp.enqueue(object : Callback<RatedTvEpisodesResponse> {
            override fun onResponse(
                call: Call<RatedTvEpisodesResponse>,
                response: Response<RatedTvEpisodesResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<RatedTvEpisodesResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun addToList(
        listId: Int,
        mediaId: MediaId,
        sessionId: String
    ): MutableLiveData<AddToListResponse> {
        val responseLiveData: MutableLiveData<AddToListResponse> = MutableLiveData()
        val resp = ApiService.create().addToList(listId, sessionId, mediaId)

        resp.enqueue(object : Callback<AddToListResponse> {
            override fun onResponse(
                call: Call<AddToListResponse>,
                response: Response<AddToListResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<AddToListResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getListInfo(listId: Int): MutableLiveData<ListResponse> {
        val responseLiveData: MutableLiveData<ListResponse> = MutableLiveData()
        val resp = ApiService.create().getListInfo(listId)

        resp.enqueue(object : Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ListResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun checkItemStatus(listId: Int, movieId: Long): MutableLiveData<CheckItemStatusResponse> {
        val responseLiveData: MutableLiveData<CheckItemStatusResponse> = MutableLiveData()
        val resp = ApiService.create().checkItemStatus(listId, movieId)

        resp.enqueue(object : Callback<CheckItemStatusResponse> {
            override fun onResponse(
                call: Call<CheckItemStatusResponse>,
                response: Response<CheckItemStatusResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CheckItemStatusResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun createNewList(
        sessionId: String,
        createNewList: CreateNewList
    ): MutableLiveData<CreateNewListResponse> {
        val responseLiveData: MutableLiveData<CreateNewListResponse> = MutableLiveData()
        val resp = ApiService.create().createNewList(sessionId, createNewList)

        resp.enqueue(object : Callback<CreateNewListResponse> {
            override fun onResponse(
                call: Call<CreateNewListResponse>,
                response: Response<CreateNewListResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CreateNewListResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun removeFromList(
        listId: Int,
        sessionId: String,
        mediaId: MediaId
    ): MutableLiveData<ResponseWithCodeAndMessage> {
        val responseLiveData: MutableLiveData<ResponseWithCodeAndMessage> = MutableLiveData()
        val resp = ApiService.create().removeFromList(listId, sessionId, mediaId)

        resp.enqueue(object : Callback<ResponseWithCodeAndMessage> {
            override fun onResponse(
                call: Call<ResponseWithCodeAndMessage>,
                response: Response<ResponseWithCodeAndMessage>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseWithCodeAndMessage>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun deleteList(
        listId: Int,
        sessionId: String
    ): MutableLiveData<ResponseWithCodeAndMessage> {
        val responseLiveData: MutableLiveData<ResponseWithCodeAndMessage> = MutableLiveData()
        val resp = ApiService.create().deleteList(listId, sessionId)

        resp.enqueue(object : Callback<ResponseWithCodeAndMessage> {
            override fun onResponse(
                call: Call<ResponseWithCodeAndMessage>,
                response: Response<ResponseWithCodeAndMessage>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseWithCodeAndMessage>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun markAsFavorite(
        accountId: Long,
        sessionId: String,
        markAsFavorite: MarkAsFavoriteRequest
    ): MutableLiveData<ResponseWithCodeAndMessage> {
        val responseLiveData: MutableLiveData<ResponseWithCodeAndMessage> = MutableLiveData()
        val resp = ApiService.create().markAsFavorite(accountId, sessionId, markAsFavorite)

        resp.enqueue(object : Callback<ResponseWithCodeAndMessage> {
            override fun onResponse(
                call: Call<ResponseWithCodeAndMessage>,
                response: Response<ResponseWithCodeAndMessage>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseWithCodeAndMessage>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun watchlist(
        accountId: Long,
        sessionId: String,
        watchlist: WatchlistRequest
    ): MutableLiveData<ResponseWithCodeAndMessage> {
        val responseLiveData: MutableLiveData<ResponseWithCodeAndMessage> = MutableLiveData()
        val resp = ApiService.create().watchlist(accountId, sessionId, watchlist)

        resp.enqueue(object : Callback<ResponseWithCodeAndMessage> {
            override fun onResponse(
                call: Call<ResponseWithCodeAndMessage>,
                response: Response<ResponseWithCodeAndMessage>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseWithCodeAndMessage>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun person(personId: Long): MutableLiveData<PersonResponse> {
        val responseLiveData: MutableLiveData<PersonResponse> = MutableLiveData()
        val resp = ApiService.create().person(personId)

        resp.enqueue(object : Callback<PersonResponse> {
            override fun onResponse(
                call: Call<PersonResponse>,
                response: Response<PersonResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<PersonResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun personImages(personId: Long): MutableLiveData<PersonImagesResponse> {
        val responseLiveData: MutableLiveData<PersonImagesResponse> = MutableLiveData()
        val resp = ApiService.create().personImages(personId)

        resp.enqueue(object : Callback<PersonImagesResponse> {
            override fun onResponse(
                call: Call<PersonImagesResponse>,
                response: Response<PersonImagesResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<PersonImagesResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun personCombinedCredits(personId: Long): MutableLiveData<CombinedCreditsResponse> {
        val responseLiveData: MutableLiveData<CombinedCreditsResponse> = MutableLiveData()
        val resp = ApiService.create().personCombinedCredits(personId)

        resp.enqueue(object : Callback<CombinedCreditsResponse> {
            override fun onResponse(
                call: Call<CombinedCreditsResponse>,
                response: Response<CombinedCreditsResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CombinedCreditsResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }
}