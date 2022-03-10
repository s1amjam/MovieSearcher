package com.moviesearcher.list

import android.content.Context
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.R
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.common.utils.Status
import com.moviesearcher.list.model.CreateNewList
import com.moviesearcher.list.model.CreateNewListResponse
import com.moviesearcher.list.model.ListResponse
import com.moviesearcher.list.model.Result
import com.moviesearcher.list.model.add.AddToListResponse
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

const val ERROR_MESSAGE = "Something went wrong '%s'"

class ListViewModel(private val listId: Int? = null, private val movieId: Long? = null) :
    ViewModel() {
    private val checkedItem = MutableLiveData<Resource<MutableMap<Int, Boolean>>>()
    private val myList = MutableLiveData<Resource<ListResponse>>()
    private val checkedItems: MutableMap<Int, Boolean> = mutableMapOf()
    private val addToList = MutableLiveData<Resource<AddToListResponse>>()
    private val createNewList = MutableLiveData<Resource<CreateNewListResponse>>()

    init {
        fetchMyList()
    }

    private fun fetchCheckedItem(listId: MutableList<Int>) {
        viewModelScope.launch {
            checkedItem.postValue(Resource.loading(null))
            try {
                coroutineScope {
                    listId.forEach {
                        val checkedItemsFromApiDeferred =
                            ApiService.create().checkItemStatus(it, movieId!!)

                        checkedItems[it] = checkedItemsFromApiDeferred.itemPresent!!
                    }
                    checkedItem.postValue(Resource.success(checkedItems))
                }
            } catch (e: Exception) {
                checkedItem.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    private fun getCheckedItem(): MutableLiveData<Resource<MutableMap<Int, Boolean>>> {
        return checkedItem
    }

    private fun fetchMyList() {
        viewModelScope.launch {
            myList.postValue(Resource.loading(null))
            try {
                val recommendationsFromApi = ApiService.create().getListInfo(listId!!)
                myList.postValue(Resource.success(recommendationsFromApi))
            } catch (e: Exception) {
                myList.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getMyList(): MutableLiveData<Resource<ListResponse>> {
        return myList
    }

    private fun fetchAddToList(listId: Int, sessionId: String, mediaId: MediaId) {
        viewModelScope.launch {
            addToList.postValue(Resource.loading(null))
            try {
                val addToListFromApi = ApiService.create().addToList(listId, sessionId, mediaId)
                addToList.postValue(Resource.success(addToListFromApi))
            } catch (e: Exception) {
                addToList.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun addToList(
        listId: Int,
        sessionId: String,
        mediaId: MediaId
    ): MutableLiveData<Resource<AddToListResponse>> {
        fetchAddToList(listId, sessionId, mediaId)

        return addToList
    }

    private fun fetchCreateNewList(sessionId: String, createNewListRequest: CreateNewList) {
        viewModelScope.launch {
            createNewList.postValue(Resource.loading(null))
            try {
                val createNewListFromApi =
                    ApiService.create().createNewList(sessionId, createNewListRequest)
                createNewList.postValue(Resource.success(createNewListFromApi))
            } catch (e: Exception) {
                createNewList.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun createNewList(
        sessionId: String,
        createNewListRequest: CreateNewList
    ): MutableLiveData<Resource<CreateNewListResponse>> {
        fetchCreateNewList(sessionId, createNewListRequest)

        return createNewList
    }

    fun showAddToListMenu(
        v: View,
        @MenuRes menuRes: Int,
        resultList: MutableList<Result>,
        media: MutableMap<String, Long>? = mutableMapOf(),
        lifecycleOwner: LifecycleOwner,
        context: Context,
        sessionId: String,
        childFragmentManager: FragmentManager
    ) {
        val popup = PopupMenu(context, v)
        val itemsId: MutableList<Int> = mutableListOf()

        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_create_new_list -> {
                    showCreateNewListDialog(childFragmentManager)
                }
            }
            true
        }

        resultList.forEach {
            popup.menu.add(Menu.NONE, it.id!!.toInt(), Menu.NONE, it.name)
            itemsId.add(it.id.toInt())
        }

        fetchCheckedItem(itemsId)

        getCheckedItem().observe(lifecycleOwner) { item ->
            when (item.status) {
                Status.SUCCESS -> {
                    item.data?.let { it1 ->
                        it1.forEach { it ->
                            val menuItem = popup.menu.findItem(it.key)

                            if (it.value) {
                                menuItem.isEnabled = false
                                if (!menuItem.title.contains("(added)")) {
                                    menuItem.title = menuItem.title.toString() + " (added)"
                                }
                            } else {
                                popup.menu.findItem(it.key).setOnMenuItemClickListener {
                                    addToList(
                                        it.itemId,
                                        sessionId,
                                        MediaId(media?.values?.first()!!)
                                    ).observe(lifecycleOwner) { item ->
                                        when (item.status) {
                                            Status.SUCCESS -> {
                                                item.data?.let {
                                                    menuItem.isEnabled = false
                                                    if (!menuItem.title.contains("(added)")) {
                                                        menuItem.title =
                                                            menuItem.title.toString() + " (added)"
                                                    }

                                                    Toast.makeText(
                                                        context,
                                                        "Added to List",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                            Status.LOADING -> {
                                            }
                                            Status.ERROR -> {
                                                Toast.makeText(
                                                    context,
                                                    "Error adding to List",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }

                                    true
                                }
                            }
                        }
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(
                        context,
                        ERROR_MESSAGE.format(item.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        popup.show()
    }

    private fun showCreateNewListDialog(childFragmentManager: FragmentManager) {
        val dialog = CreateNewListDialog(this)
        dialog.show(childFragmentManager, "CreateNewListDialogFragment")
    }
}