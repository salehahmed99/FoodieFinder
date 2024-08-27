package com.example.foodapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.UserDao
import com.example.foodapp.pojo.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userDao : UserDao) : ViewModel() {
    private val _name : MutableLiveData<String> = MutableLiveData()
    val name : LiveData<String> = _name

    fun getUserNameById(userId : String){
        viewModelScope.launch(Dispatchers.IO){
            val userName = userDao.getUserNameById(userId)
            withContext(Dispatchers.Main){
                _name.postValue(userName)
            }
        }
    }

    fun addUser(user : User){
        viewModelScope.launch(Dispatchers.IO){
            val result = userDao.addUser(user)
            withContext(Dispatchers.Main){
                if (result > 0){
                    _name.postValue(user.name)
                }
            }
        }
    }
}

class UserFactory(private val userDao : UserDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(userDao) as T
        }
        else
            throw IllegalArgumentException()
    }
}