package com.example.rakshak

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import java.awt.Button

import javax.swing.text.View

import butterknife.ButterKnife
import butterknife.InjectView
import sun.rmi.runtime.Log

class LoginActivity : AppCompatActivity() {

    @InjectView(R.id.input_email)
    internal var _emailText: EditText? = null
    @InjectView(R.id.input_password)
    internal var _passwordText: EditText? = null
    @InjectView(R.id.btn_login)
    internal var _loginButton: Button? = null
    @InjectView(R.id.link_signup)
    internal var _signupLink: TextView? = null

    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.inject(this)

        _loginButton!!.setOnClickListener(object : View.OnClickListener() {

            fun onClick(v: View) {
                login()
            }
        })

        _signupLink!!.setOnClickListener(object : View.OnClickListener() {

            fun onClick(v: View) {
                // Start the Signup activity
                val intent = Intent(getApplicationContext(), SignupActivity::class.java)
                startActivityForResult(intent, REQUEST_SIGNUP)
            }
        })
    }

    fun login() {
        Log.d(TAG, "Login")

        if (!validate()) {
            onLoginFailed()
            return
        }

        _loginButton!!.setEnabled(false)

        val progressDialog = ProgressDialog(this@LoginActivity,
                R.style.AppTheme_Dark_Dialog)
        progressDialog.setIndeterminate(true)
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()

        val email = _emailText!!.getText().toString()
        val password = _passwordText!!.getText().toString()

        // TODO: Implement your own authentication logic here.

        android.os.Handler().postDelayed(
                object : Runnable {
                    override fun run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess()
                        // onLoginFailed();
                        progressDialog.dismiss()
                    }
                }, 3000)
    }


    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish()
            }
        }
    }

    fun onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true)
    }

    fun onLoginSuccess() {
        _loginButton!!.setEnabled(true)
        finish()
    }

    fun onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show()

        _loginButton!!.setEnabled(true)
    }

    fun validate(): Boolean {
        var valid = true

        val email = _emailText!!.getText().toString()
        val password = _passwordText!!.getText().toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText!!.setError("enter a valid email address")
            valid = false
        } else {
            _emailText!!.setError(null)
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            _passwordText!!.setError("between 4 and 10 alphanumeric characters")
            valid = false
        } else {
            _passwordText!!.setError(null)
        }

        return valid
    }

    companion object {
        private val TAG = "LoginActivity"
        private val REQUEST_SIGNUP = 0
    }
}