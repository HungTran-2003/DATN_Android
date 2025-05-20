package haui.do_an.moive_ticket_booking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import haui.do_an.moive_ticket_booking.repository.OtpReponsitory
import haui.do_an.moive_ticket_booking.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val otpReponsitory: OtpReponsitory,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _otpsend = MutableLiveData<String>()
    val otpsend: LiveData<String> = _otpsend

    private val _otpConfirm = MutableLiveData<Boolean>()
    val otpConfirm: LiveData<Boolean> = _otpConfirm

    private val _registerSuccess = MutableLiveData<String>()
    val registerSuccess: LiveData<String> = _registerSuccess

    private val _loginSuccess = MutableLiveData<Map<String, String>>()
    val loginSuccess: LiveData<Map<String, String>> = _loginSuccess



    var usename: String? = null
    var password: String? = null
    var email: String? = null

    fun getOTP(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when(email){
                    null ->  _errorMessage.postValue("không có email")
                    else -> {
                        val result = otpReponsitory.getOtp(email!!)
                        _otpsend.postValue(result)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message.toString())
            }
        }
    }

    fun sendOtp(otp : String){
        if(otp.isEmpty() || email == null) return
        val map = mapOf("email" to email!!, "otp" to otp)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result: Boolean = otpReponsitory.sendOtp(map)
                _otpConfirm.postValue(result)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message.toString())
            }
        }
    }

    fun saveTermUser(username: String, password: String, email: String){
        this.usename = username
        this.password = password
        this.email = email
    }

    fun createUser(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (usename == null || password == null || email == null){
                    _errorMessage.postValue("không có dữ liệu")
                } else {
                    val result = userRepository.createUser(usename!!, email!!, password!!)
                    _registerSuccess.postValue(result)
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message.toString())
            }
        }
    }

    fun loginUser(username: String, password: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result: Map<String, String> = userRepository.login(username, password)
                _loginSuccess.postValue(result)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message.toString())
            }
        }
    }


//    private fun hasActiveInternetConnection(): Boolean {
//        return try {
//            val socket = Socket()
//            socket.connect(InetSocketAddress("8.8.8.8", 53), 5000) // Ping Google DNS trên cổng 53
//            socket.close()
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    fun checkInternetConnection() {
//        CoroutineScope(Dispatchers.Main).launch {
//            while (_networkError.value == false){
//                delay(5000)
//                _networkError.value = withContext(Dispatchers.IO) {
//                    hasActiveInternetConnection()
//                }
//            }
//        }
//    }
//
//    fun onRetry() {
//        checkInternetConnection()
//    }



}