package com.atitienei_daniel.reeme.presentation.ui.screens.register

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private var _emailError = mutableStateOf<String?>(null)
    val emailError: State<String?> = _emailError

    private var _passwordError = mutableStateOf<String?>(null)
    val passwordError: State<String?> = _passwordError

    private var _nameError = mutableStateOf<String?>(null)
    val nameError: State<String?> = _nameError

    init {
        Log.d("CurrentUser", auth.currentUser?.email.toString())
    }

    fun registerUser(email: String, name: String, password: String, onAccountCreated: () -> Unit) {
        if (email.isEmpty())
            _emailError.value = "Email shouldn't be empty."
        if (name.isEmpty())
            _nameError.value = "Name shouldn't be empty."
        else if (!name.matches(Regex("^[A-Z][a-z]*(([,.] |[ '-])[A-Za-z][a-z]*)*(\\.?)\$")))
            _nameError.value = "Name shouldn't contain numbers or special characters."
        if (password.isEmpty())
            _passwordError.value = "Password shouldn't be empty."

        if (!_emailError.value.isNullOrEmpty() || !_nameError.value.isNullOrEmpty() || !_passwordError.value.isNullOrEmpty())
            return

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                try {
                    if (task.isSuccessful) {
                        Log.d("Registration", "SUCCESS")

                        firestore.collection("users").document(auth.currentUser?.uid.toString())
                            .set(
                                hashMapOf(
                                    "email" to email,
                                    "name" to name
                                )
                            )
                            .addOnSuccessListener {
                                Log.d("CreateAccount", "SUCCESS")
                                onAccountCreated()
                            }

                    } else {
                        throw task.exception!!
                    }
                } catch (e: FirebaseAuthUserCollisionException) {
                    _emailError.value = e.message!!
                    Log.d("Registration", _emailError.value!!)
                } catch (e: FirebaseAuthWeakPasswordException) {
                    _passwordError.value = e.message!!
                    Log.d("Registration", _passwordError.value!!)
                } catch (e: FirebaseAuthException) {
                    Log.d("Registration", e.message!!)
                }
            }
    }

    fun clearEmailError() {
        _emailError.value = null
    }

    fun clearPasswordError() {
        _passwordError.value = null
    }

    fun clearNameError() {
        _nameError.value = null
    }
}