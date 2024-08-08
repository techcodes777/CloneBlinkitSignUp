package com.eclatsol.userblinkitclone.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.eclatsol.userblinkitclone.Utils
import com.eclatsol.userblinkitclone.models.Users
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {
    private val _veerificationId = MutableStateFlow<String?>(null)

    private val _otpSent = MutableStateFlow(false)
    val otpSent = _otpSent

    private val _isSignedInSuccessfully = MutableStateFlow(false)
    val isSignedInSuccessfully = _isSignedInSuccessfully

    private val _isCurrentUser = MutableStateFlow(false)
    val isCurrentUser = _isCurrentUser

    init {
        Utils.getAuthInstance().currentUser?.let {
            _isCurrentUser.value = true
        }
    }
    fun sendOtp(userNumber: String, activity: Activity) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _veerificationId.value = verificationId
                otpSent.value = true
            }
        }

        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber("+91$userNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(otp: String, userNumber: String, user: Users) {
        val credential = PhoneAuthProvider.getCredential(_veerificationId.value.toString(), otp)
        Utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseDatabase.getInstance().getReference("AllUsers").child("Users")
                        .child(user.uid!!).setValue(user)
                    _isSignedInSuccessfully.value = true
                } else {

                }
            }
    }
}