package com.example.uthsmarttasks.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var verificationId: String? = null

    // --- Email/Password ---
    fun signInWithEmail(email: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _isSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Đăng nhập thất bại"
                }
            }
    }

    // --- Google ---
    fun signInWithGoogle(idToken: String) {
        _isLoading.value = true
        _errorMessage.value = null

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        _isSuccess.value = true
                    } else {
                        _errorMessage.value = task.exception?.message ?: "Đăng nhập thất bại"
                    }
                }
        }
    }
    fun signInAnonymously() {
        _isLoading.value = true
        _errorMessage.value = null

        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _isSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Đăng nhập ẩn danh thất bại"
                }
            }
    }


    fun resetState() {
        _isSuccess.value = false
        _errorMessage.value = null
    }
}
