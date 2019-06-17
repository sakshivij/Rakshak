package com.example.rakshak

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_dashboard.*
import android.util.Log
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.R
import android.graphics.Movie





class DashboardActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val contactList: MutableList<Contact> = ArrayList()
    //private val contactList = listOf<Contact>()
    internal val context: Context = this
    private var button1: Button? = null
    private var result: EditText? = null
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ContactAdapter? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                this.add_contact.setOnClickListener{
                    recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView

                    mAdapter = ContactAdapter(contactlist)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    recyclerView.setLayoutManager(mLayoutManager)
                    recyclerView.setItemAnimator(DefaultItemAnimator())
                    recyclerView.setAdapter(mAdapter)
                }
                //textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //textMessage.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        val button1 : Button = findViewById(R.id.add_contact)
        button1!!.setOnClickListener(object: View.OnClickListener {

            override fun onClick(arg0:View) {

                // get prompts.xml view
                val li = LayoutInflater.from(context)
                val promptsView = li.inflate(R.layout.prompt, null)

                val alertDialogBuilder = AlertDialog.Builder(
                    context)

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView)

                val userInput1 = promptsView
                    .findViewById(R.id.userNameInput) as EditText

                val userInput2 = promptsView
                    .findViewById(R.id.userNumberInput) as EditText

                // set dialog message
                alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK"
                    ) { dialog, id ->
                        // get user input and set it to result
                        // edit text
                        //result!!.setText(userInput.text)
                        var contact = Contact(userInput1.toString(),userInput2.toString());
                        contactList.add(contact);
                        Log.v(null, "Added to list"+contactList.toString())
                        mAdapter.notifyDataSetChanged();
                    }
                    .setNegativeButton("Cancel",
                        object: DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, id:Int) {
                                dialog.cancel()
                            }
                        })

                // create alert dialog
                val alertDialog = alertDialogBuilder.create()

                // show it
                alertDialog.show()

            }
        })
    }
}
