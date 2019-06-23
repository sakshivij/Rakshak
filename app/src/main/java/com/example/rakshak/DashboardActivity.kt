package com.example.rakshak

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat
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
import android.telephony.SmsManager
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import java.util.*
import kotlin.collections.ArrayList


class DashboardActivity : AppCompatActivity() {

    private lateinit var userInput1 : EditText;
    private lateinit var userInput2 : EditText;
    private lateinit var message : String;
    private lateinit var phoneNum : String;
    private lateinit var textMessage: TextView
    private val contactList: ArrayList<Contact> = ArrayList()
    internal val context: Context = this
    private var button1: Button? = null
    private var result: EditText? = null
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ContactAdapter? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var geocoder = Geocoder(this, Locale.getDefault())
    protected var mLastLocation: Location? = null
    protected val REQUEST_CHECK_SETTINGS = 0x1
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                //textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //textMessage.setText(R.string.title_dashboard)
                //this.progressBarSendMessage.visibility = View.VISIBLE
                this.sendbutton.visibility = View.VISIBLE;
                //sendSMSMessage();
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //textMessage.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result

                    /*
                    mLatitudeText!!.setText(
                        mLatitudeLabel+":   "+
                                (mLastLocation )!!.latitude)
                    mLongitudeText!!.setText(mLongitudeLabel+":   "+
                            (mLastLocation )!!.longitude)*/
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    //showMessage(getString(R.string.no_location_detected))
                }
            }
    }
/*
    private fun showMessage(text: String) {
        //val container = findViewById<View>(R.id.das)
        //if (container != null) {
            Toast.makeText(this@DashboardActivity, text, Toast.LENGTH_LONG).show()
        //}
    }
*/


    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {

        Toast.makeText(this@DashboardActivity, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }



    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@DashboardActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }


    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

            /*showSnackbar(R.string.permission_rationale, android.R.string.ok,
                View.OnClickListener {
                    // Request permission
                    startLocationPermissionRequest()
                })*/

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                /*showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })*/
            }
        }
    }
    fun createLocationRequest() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val button1 : FloatingActionButton = this.findViewById(R.id.add_contact)
        Log.v(null, "In click");
        val sendButton : Button = findViewById(R.id.sendbutton)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //geocoder = Geocoder(this, Locale.getDefault())

            /*fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                    // Got last known location. In some rare situations, this can be null.
                    if (location != null) {
                        // Logic to handle location object
                    }
                })*/
        sendButton.setOnClickListener (object: View.OnClickListener {

            override fun onClick(arg0:View) {
                progressBarSendMessage.visibility = View.VISIBLE
                sendSMSMessage();
            }
        })


        button1!!.setOnClickListener(object: View.OnClickListener {

            override fun onClick(arg0:View) {

                recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView

                mAdapter = ContactAdapter(contactList)
                val mLayoutManager = LinearLayoutManager(applicationContext)
                recyclerView?.setLayoutManager(mLayoutManager)
                recyclerView?.setItemAnimator(DefaultItemAnimator())
                recyclerView?.setAdapter(mAdapter)

                Log.v(null, "In click listener");
                // get prompts.xml view
                val li = LayoutInflater.from(context)
                val mView = li.inflate(R.layout.prompt, null)

                val alertDialogBuilder = AlertDialog.Builder(
                    context)

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(mView)

                userInput1 = mView
                    .findViewById(R.id.userNameInput) as EditText

                Log.e(null, "Name")
                userInput2 = mView
                    .findViewById(R.id.userNumberInput) as EditText

                // set dialog message
                alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK"
                    ) { dialog, id ->
                        // get user input and set it to result
                        // edit text
                        //result!!.setText(userInput.text)
                        var contact = Contact(userInput1.text.toString(),userInput2.text.toString());
                        contactList.add(contact);
                        Log.d("TAG",userInput1.toString());
                        //Log.v(null, "Added to list"+contactList.get(0).name)
                        mAdapter?.notifyDataSetChanged();
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

            }//end of oncreate

/*
    object {
        public val MY_PERMISSIONS_REQUEST_SEND_SMS = 0
    }
*/
    fun sendSMSMessage() {
    phoneNum = userInput2.text.toString();
    Log.d(null, "phone number: "+ phoneNum);
   // var addresses: List<Address> = emptyList()
  //  Log.d(TAG,"lomgitude"+ mLastLocation!!.longitude.toString())
//    addresses = geocoder.getFromLocation( mLastLocation!!.latitude,mLastLocation!!.longitude,
        // In this sample, we get just a single address.
     //   1)
    //val addr : String = addresses.get(0).getAddressLine(0)
    message = "In an accident! Please help! Location"//+ addr;

    /*Log.d(null, "In message")

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.SEND_SMS),
                    MY_PERMISSIONS_REQUEST_SEND_SMS
                )
            }
            }*/
    try {
        val smgr = SmsManager.getDefault()
        smgr.sendTextMessage(phoneNum, null, message, null, null)
        this.progressBarSendMessage.visibility = View.GONE
        Toast.makeText(this@DashboardActivity, "SMS Sent Successfully", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(this@DashboardActivity, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show()
    }

}


/*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {z
            MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phoneNum, null, message, null, null)
                    this.progressBarSendMessage.visibility = View.GONE;
                    Toast.makeText(
                        applicationContext, "SMS sent.",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    Toast.makeText(
                        applicationContext,
                        "SMS faild, please try again.", Toast.LENGTH_LONG
                    ).show()
                    return
                }
            }
        }
    }*/


        companion object {

            private val TAG = "LocationProvider"

            private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        }
}
