package com.example.wander.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wander.network.Network

class SignUpViewModel : ViewModel() {

    val _username = MutableLiveData<String>()
    val _firstName = MutableLiveData<String>()
    val _lastName = MutableLiveData<String>()
    val _email = MutableLiveData<String>()
    val _password = MutableLiveData<String>()
    val _passConfirm = MutableLiveData<String>()

    val buttonEnabled = MediatorLiveData<Boolean>()

    val _errorMessage=MutableLiveData<String>()
    val network=Network.getNetworkProvider()
    val networkState: LiveData<Network.NetworkState> get() =Network.networkCurrentState
    val errorMessage: LiveData<String> get() = _errorMessage

    init {

        _username.value = ""
        _firstName.value = ""
        _lastName.value = ""
        _email.value = ""
        _password.value = ""
        _passConfirm.value = ""

        buttonEnabled.addSource(_username) {
            buttonEnabled.value = validateButton(
                _username, _firstName, _lastName,
                _email, _password, _passConfirm
            )
        }

        buttonEnabled.addSource(_firstName) {
            buttonEnabled.value = validateButton(
                _username, _firstName, _lastName,
                _email, _password, _passConfirm
            )
        }
        buttonEnabled.addSource(_lastName) {
            buttonEnabled.value = validateButton(
                _username, _firstName, _lastName,
                _email, _password, _passConfirm
            )
        }

        buttonEnabled.addSource(_email) {
            buttonEnabled.value = validateButton(
                _username, _firstName, _lastName,
                _email, _password, _passConfirm
            )
        }
        buttonEnabled.addSource(_password) {
            buttonEnabled.value = validateButton(
                _username, _firstName, _lastName,
                _email, _password, _passConfirm
            )
        }

        buttonEnabled.addSource(_passConfirm) {
            buttonEnabled.value = validateButton(
                _username, _firstName, _lastName,
                _email, _password, _passConfirm
            )
        }
    }

    fun onTextChangedUserName(
        s: CharSequence
    ) {
        _username.value=s.toString()
    }

    fun onTextChangedFirstName(
        s: CharSequence
    ) {
        _firstName.value=s.toString()
    }

    fun onTextChangedLastName(
        s: CharSequence
    ) {
        _lastName.value=s.toString()
    }

    fun onTextChangedEmail(
        s: CharSequence
    ) {
        _email.value=s.toString()
    }

    fun onTextChangedPw(
        s: CharSequence
    ) {
        _password.value=s.toString()
    }
    fun onTextChangedPwConfirm(
        s: CharSequence
    ) {
        _passConfirm.value=s.toString()
    }

    private fun validateButton(
        _username: MutableLiveData<String>,
        _firstName: MutableLiveData<String>,
        _lastName: MutableLiveData<String>,
        _email: MutableLiveData<String>,
        _password: MutableLiveData<String>,
        _passConfirm: MutableLiveData<String>
    ): Boolean {
        val un = _username.value!!
        val fn = _firstName.value!!
        val ln = _lastName.value!!
        val em = _email.value!!
        val p1 = _password.value!!
        val p2 = _passConfirm.value!!


        return (un.isNotBlank() && un.isNotEmpty()) &&
                (fn.isNotBlank() && fn.isNotEmpty()) &&
                (ln.isNotBlank() && ln.isNotEmpty()) &&
                (em.isNotBlank() && em.isNotEmpty()) &&
                (p1.isNotBlank() && p1.isNotEmpty()) &&
                (p2.isNotBlank() && p2.isNotEmpty()) &&
                (p2==p1)

    }

}
