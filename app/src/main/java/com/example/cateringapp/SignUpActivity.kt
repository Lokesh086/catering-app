package com.example.cateringapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.cateringapp.data.Customer
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private val authService = FirebaseAuthRepository.getInstance()

    private val backButton by lazy {
        findViewById<ImageButton>(R.id.sign_up_back)
    }
    private val fullNameInput by lazy {
        findViewById<EditText>(R.id.full_name)
    }
    private val emailInput by lazy {
        findViewById<EditText>(R.id.email)
    }
    private val passwordInput by lazy {
        findViewById<EditText>(R.id.password)
    }
    private val addressInput by lazy {
        findViewById<EditText>(R.id.address)
    }
    private val signUpButton by lazy {
        findViewById<Button>(R.id.sign_up_btn)
    }
    private val loading by lazy {
        findViewById<ProgressBar>(R.id.loading)
    }

    private var isLoading = false

    private var fullName = ""
    private var email = ""
    private var password = ""
    private var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        if ( authService.getUser() != null ) {
            gotoHome()
        }

        // Input field handlers
        fullNameInput.doOnTextChanged { text, _, _, _ -> fullName = text.toString() }
        emailInput.doOnTextChanged { text, _, _, _ -> email = text.toString() }
        passwordInput.doOnTextChanged { text, _, _, _ -> password = text.toString() }
        addressInput.doOnTextChanged { text, _, _, _ -> address = text.toString() }

        // Button click actions
        backButton.setOnClickListener { finish() }
        signUpButton.setOnClickListener {
            launchSignUpFlow()
        }

    }

    // Call createUser function from FirebaseAuthRepository
    private fun launchSignUpFlow() {
        if ( fullName !== "" && email !== "" && password !== "" && address !== "" ) {
            toggleLoading()
            lifecycleScope.launch {
                val result = authService.createUser(Customer(fullName, email, address), password)
                if ( result.user != null ) {
                    gotoHome()
                } else {
                    toggleLoading()
                    showAlert(result.exception?.message.toString())
                }
            }
        }
    }

    // Open CustomerHome activity
    private fun gotoHome() {
        Intent(this, CustomerHome::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.run {
            startActivity(this)
        }
    }

    // Switch loading
    private fun toggleLoading() {
        if ( isLoading ) {
            loading.visibility = ProgressBar.GONE
            signUpButton.visibility = Button.VISIBLE
        } else {
            signUpButton.visibility = Button.GONE
            loading.visibility = ProgressBar.VISIBLE
        }
        isLoading = !isLoading
    }

    // Show resultant message
    private fun showAlert(msg: String) {
        AlertDialog.Builder(this).apply {
            title = "Sign Up Failed"
            setMessage(msg)
            setPositiveButton("Ok", null)
        }.run {
            val alertDialog = this.create()
            alertDialog.show()
        }
    }
}