package com.example.wander.fragments

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wander.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    private var viewModelJob: Job = Job()
    private var couroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    private val _response = MutableLiveData<Result<Token>>()
    private val _buttonEnabled = MediatorLiveData<Boolean>()


    val network = Network.getNetworkProvider()
    val response: LiveData<Result<Token>> get() = _response
    val buttonEnabled: LiveData<Boolean> get() = _buttonEnabled

    init{
        _email.value = ""
        _password.value = ""

        _buttonEnabled.addSource(_email) {
            _buttonEnabled.value = validateButton(
                _email, _password
            )
        }

        _buttonEnabled.addSource(_password) {
            _buttonEnabled.value = validateButton(
                _email, _password
            )
        }
    }

    private fun validateButton(
        _email: MutableLiveData<String>,
        _password: MutableLiveData<String>
    ): Boolean {

        val em = _email.value!!
        val p1 = _password.value!!

        return  (em.isNotBlank() && em.isNotEmpty()) &&
                (p1.isNotBlank() && p1.isNotEmpty())

    }

    fun login() {

        val user = Login(
            email = _email.value ?: "",
            password = _password.value ?: ""
        )

        couroutineScope.launch {
            network.loginUser(user,onSuccess = {
                _response.postValue(Result.Success(it))
            },onError = {
                _response.postValue(Result.Error(it))
            })
        }

    }
}
