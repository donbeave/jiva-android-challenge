package com.zhokhov.jiva.challenge.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.zhokhov.jiva.challenge.R
import com.zhokhov.jiva.challenge.databinding.ActivityLoginBinding
import com.zhokhov.jiva.challenge.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        bindState()
        bindUiControls()
    }

    private fun bindState() {
        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            binding.loginButton.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                binding.emailInputBox.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                binding.passwordInputBox.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginCredentials.observe(this, Observer {
            val loginCredentials = it ?: return@Observer

            binding.emailInputBox.setText(
                loginCredentials.email, TextView.BufferType.EDITABLE
            )
            binding.passwordInputBox.setText(
                loginCredentials.password, TextView.BufferType.EDITABLE
            )
        })

        loginViewModel.loginState.observe(this, { loginState ->
            setLoading(false)

            when (loginState) {
                is LoginError -> {
                    Toast.makeText(applicationContext, loginState.error, Toast.LENGTH_SHORT).show()
                }
                is LoginSuccess -> {
                    Intent(this, ProfileActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NEW_TASK

                        startActivity(this)
                    }
                }
            }
        })
    }

    private fun bindUiControls() {
        binding.emailInputBox.doAfterTextChanged {
            loginViewModel.credentialsChanged(
                binding.emailInputBox.text.toString(),
                binding.passwordInputBox.text.toString()
            )
        }

        binding.passwordInputBox.doAfterTextChanged {
            loginViewModel.credentialsChanged(
                binding.emailInputBox.text.toString(),
                binding.passwordInputBox.text.toString()
            )
        }

        binding.loginButton.setOnClickListener {
            setLoading(true)

            loginViewModel.login(
                binding.emailInputBox.text.toString(),
                binding.passwordInputBox.text.toString()
            )
        }
    }

    fun setLoading(loading: Boolean) {
        if (loading) {
            binding.loading.visibility = View.VISIBLE
            binding.loginButton.text = getString(R.string.loading)
            binding.loginButton.isEnabled = false
            binding.emailInputBox.isEnabled = false
            binding.passwordInputBox.isEnabled = false
        } else {
            binding.loading.visibility = View.GONE
            binding.loginButton.text = getString(R.string.action_login)
            binding.loginButton.isEnabled = true
            binding.emailInputBox.isEnabled = true
            binding.passwordInputBox.isEnabled = true
        }
    }

}
