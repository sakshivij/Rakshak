package com.example.rakshak

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.rakshak.R
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RegistrationActivity : AppCompatActivity() {
    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var phnumber : EditText? = null
    private var uname : EditText? = null
    private var bloodgroup : EditText? = null

    private var regBtn: Button? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase : FirebaseDatabase? = null
    private var mDatabaseReference : FirebaseDatabase? = null
    private var dataList : HashMap<String,String>? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registartion)

        mAuth = FirebaseAuth.getInstance()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        mDatabase = FirebaseDatabase.getInstance()

        Log.v(null,"Auth variable"+mAuth);
        initializeUI()
        regBtn!!.setOnClickListener { registerNewUser() }

        //ValueEventListener(mDatabaseReference)
    }

    private fun registerNewUser() {
        progressBar!!.visibility = View.VISIBLE

        val email: String
        val password: String
        email = emailTV!!.text.toString()
        password = passwordTV!!.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()
                    val key = FirebaseDatabase.getInstance().getReference().child("data").push().getKey();
                    Log.wtf(null,uname!!.text.toString())
                    dataList!!.put("name", uname!!.text.toString())
                    dataList!!.put("email", emailTV!!.text.toString())
                    dataList!!.put("password", passwordTV!!.text.toString())
                    dataList!!.put("phone", phnumber!!.text.toString())
                    dataList!!.put("bloodGroup", bloodgroup!!.text.toString())
                    val childUpdate : HashMap<String, HashMap<String,String>> = HashMap()
                    childUpdate!!.put("/data/"+key , dataList!!);
                    FirebaseDatabase.getInstance().getReference().updateChildren(childUpdate as Map<String, Any>)
                    progressBar!!.visibility = View.GONE

                     val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(null, "onComplete: Failed=" + task.getException());
                    Toast.makeText(applicationContext, "Registration failed! Please try again later", Toast.LENGTH_LONG)
                        .show()
                    progressBar!!.visibility = View.GONE
                }
            }

    }
    private fun initializeUI() {
        emailTV = findViewById(R.id.email)
        passwordTV = findViewById(R.id.password)
        regBtn = findViewById(R.id.register)
        progressBar = findViewById(R.id.progressBar)
        uname = findViewById(R.id.name)
        phnumber = findViewById(R.id.phone)
        bloodgroup = findViewById(R.id.bg)

    }
}