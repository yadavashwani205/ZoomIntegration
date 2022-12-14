package com.example.zoomkotlinproject.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zoomkotlinproject.BuildConfig
import com.example.zoomkotlinproject.R
import com.example.zoomkotlinproject.adapter.MeetingAdapter
import com.example.zoomkotlinproject.databinding.ActivityMainBinding
import com.example.zoomkotlinproject.model.Meeting
import com.example.zoomkotlinproject.utils.Constants
import com.example.zoomkotlinproject.utils.SharedPref
import com.example.zoomkotlinproject.viewmodel.MainViewModel
import com.example.zoomkotlinproject.viewstate.MainViewEvent
import com.example.zoomkotlinproject.viewstate.MainViewState
import com.google.android.material.snackbar.Snackbar
import us.zoom.sdk.*


class MainActivity : AppCompatActivity(), MeetingClickListener {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var meeting: Meeting? = null
    private lateinit var zoomSdk: ZoomSDK
    private var isAudible = false
    private var doubleBackToExitPressedOnce = false
    private val handler = Handler(Looper.getMainLooper())
    private val permissionRequestReadContacts = 1000

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.viewState.observe(this) { render(it) }
        zoomSdk = ZoomSDK.getInstance()
        supportActionBar?.hide()
        initializeZoomSDK()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finish()
                }
                doubleBackToExitPressedOnce = true
                snackBar(getString(R.string.please_click_back))
                Handler(Looper.getMainLooper()).postDelayed(
                    { doubleBackToExitPressedOnce = false },
                    2000
                )
            }
        })
        mBinding.welcomeTv.text = "Welcome ${SharedPref.readPrefString(this, Constants.USER_NAME)}"
        val versionCode = BuildConfig.VERSION_CODE
        if (SharedPref.readPrefInt(this, Constants.MIN_APP_VERSION) > versionCode) {
            showAlertDialog(getString(R.string.min_app_version_available_on_play_store), false)
        } else if (intent.getBooleanExtra(Constants.IS_NOW_LOGGED_IN, false)) {
            if (SharedPref.readPrefInt(this, Constants.LATEST_APP_VERSION) > versionCode) {
                showAlertDialog(getString(R.string.latest_app_available_on_play_store), true)
            }
        }
        mBinding.scheduleBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.main_container,
                MatchSchedulerFragment(),
                "MatchSchedulerFragment"
            )
            transaction.addToBackStack(null)
            transaction.commit()
        }
        mBinding.logoutBtn.setOnClickListener {
            if (Constants.isOnline(this)) {
                logoutDialog()
            } else {
                snackBar(getString(R.string.you_are_offline_please_connect_to_internet))
            }
        }
        mBinding.changePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        mBinding.refreshBtn.setOnClickListener {
            if (Constants.isOnline(this)) {
                callMeetingApi()
            } else {
                snackBar(getString(R.string.you_are_offline_please_connect_to_internet))
            }
        }
        if (Constants.isOnline(this)) {
            callMeetingApi()
        } else {
            snackBar(getString(R.string.you_are_offline_please_connect_to_internet))
        }
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (Constants.isOnline(this@MainActivity))
                    viewModel.onEvent(
                        MainViewEvent.VerifyTokenEvent(
                            this@MainActivity,
                            "Bearer ${
                                SharedPref.readPrefString(
                                    this@MainActivity,
                                    Constants.TOKEN
                                )
                            }"
                        )
                    )
                handler.postDelayed(this, 1000 * 60 * 1)
            }
        }, 1000 * 60 * 1)
    }

    private fun callMeetingApi() {
        mBinding.progressBar.visibility = View.VISIBLE
        viewModel.onEvent(
            MainViewEvent.GetMeetingEvent(
                this,
                "Bearer ${
                    SharedPref.readPrefString(
                        this,
                        Constants.TOKEN
                    )
                }"
            )
        )
    }

    private fun render(viewState: MainViewState?) {
        if (viewState == null) return

        viewState.meetingResponse?.let {
            mBinding.progressBar.visibility = View.GONE
            if (it.data != null) {
                it.data.remainingDays?.let { it1 ->
                    SharedPref.writePrefString(
                        this, Constants.REMAINING_DAYS,
                        it1
                    )
                }
                if (it.data.remainingDays == "0") {
                    mBinding.expireAccountTime.text =
                        getString(R.string.your_account_expired)
                } else {
                    mBinding.expireAccountTime.text =
                        getString(R.string.your_account_will_expire_in, "${it.data.remainingDays}")
                    initializeMeetingAdapter(it.data.meeting)
                }
            }
        }

        viewState.error?.let {
            mBinding.progressBar.visibility = View.GONE
            snackBar(it)
        }
        viewState.logoutResponse?.let {
            finish()
            mBinding.progressBar.visibility = View.VISIBLE
            snackBar(it.message.toString())
            SharedPref.clear(this)
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun initializeMeetingAdapter(data: List<Meeting>?) {
        val adapter = MeetingAdapter(this)
        mBinding.meetingRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.meetingRv.adapter = adapter
        adapter.submitList(data)
    }

    private fun logoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Do you really want to sign out")
        builder.setPositiveButton("yes") { _, _ ->
            viewModel.onEvent(
                MainViewEvent.LogoutEvent(
                    this,
                    "Bearer ${
                        SharedPref.readPrefString(
                            this,
                            Constants.TOKEN
                        )
                    }"
                )
            )
        }
        builder.setNegativeButton("N0") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    //for starting and initializing meeting
    override fun startMeeting(meeting: Meeting, isAudible: Boolean) {
        mBinding.progressBar.visibility = View.VISIBLE
        this.isAudible = isAudible
        this.meeting = meeting
        startZoomMeeting()
    }

    private fun initializeZoomSDK() {
        Log.d("startedMeeting", meeting.toString())
        zoomSdk
        val params = ZoomSDKInitParams().apply {
            appKey = SharedPref.readPrefString(this@MainActivity, Constants.APP_KEY)
            appSecret = SharedPref.readPrefString(this@MainActivity, Constants.APP_SECRET)
            domain = "zoom.us"
            enableLog = true
        }
        val listener = object : ZoomSDKInitializeListener {
            override fun onZoomSDKInitializeResult(p0: Int, p1: Int) {
                if (p0 == 0) {
//                    startZoomMeeting()
//                    showContacts()
                } else {
                    mBinding.progressBar.visibility = View.GONE
                    initializeErrorCode(p0)
                }
            }

            override fun onZoomAuthIdentityExpired() {

            }
        }
        zoomSdk.initialize(this, listener, params)
    }

    private fun startZoomMeeting() {
        if (zoomSdk.isInitialized) {
            val meetingService = ZoomSDK.getInstance().meetingService
            val meetingOptions = JoinMeetingOptions().apply {
                no_driving_mode = true
                no_bottom_toolbar = true
                no_titlebar = true
                no_audio = isAudible
                custom_meeting_id = "nxtLive"
                no_invite = true
                meeting_views_options =
                    MeetingViewsOptions.NO_BUTTON_PARTICIPANTS + MeetingViewsOptions.NO_TEXT_MEETING_ID + MeetingViewsOptions.NO_TEXT_PASSWORD
            }
            zoomSdk.meetingSettingsHelper.isHideNoVideoUsersEnabled = true
            zoomSdk.zoomUIService.hideMeetingInviteUrl(true)
            val params = JoinMeetingParams().apply {
                displayName = meeting?.customerName
                meetingNo = meeting?.meetingNo
                this.password = meeting?.password
            }
            meetingService.joinMeetingWithParams(this, params, meetingOptions)
            mBinding.progressBar.visibility = View.GONE
        } else {
            mBinding.progressBar.visibility = View.GONE
            snackBar(getString(R.string.unbable_to_connect_sdk))
        }
    }

    //update app
    private fun showAlertDialog(message: String, showNegativeButton: Boolean) {
        val alertDialog = AlertDialog.Builder(this).setTitle("Update!!").setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ ->
                updateApp()
            }.setCancelable(false)
        if (showNegativeButton)
            alertDialog.setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
        alertDialog.show()
    }

    private fun updateApp() {
        val appPackageName = packageName

        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    private fun initializeErrorCode(code: Int) {
        when (code) {
            1 -> snackBar(getString(R.string.invalid_arguments))
            2 -> snackBar(getString(R.string.illigal_app_key_or_secret))
            3 -> snackBar(getString(R.string.zm_mm_msg_network_unavailable))
            4 -> snackBar(getString(R.string.authenticate_client_incompatible))
            5 -> snackBar(getString(R.string.token_wrong))
            6 -> snackBar(getString(R.string.illigal_app_key_or_secret))
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        if (zoomSdk.isInitialized) {
            zoomSdk.meetingService.leaveCurrentMeeting(true)
        }
        super.onDestroy()
    }

/*
    private fun showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                permissionRequestReadContacts
            )
        } else {
            getContactList()
        }
    }
*/

/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestReadContacts) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getContactList()
            }
        }
    }
*/

 /*   @SuppressLint("Range")
    private fun getContactList() {
        val hashMap = hashMapOf<String, String>()
        val cr = contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String? = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur?.moveToNext() == true) {
                        val phoneNo: String = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        hashMap[name ?: ""] = phoneNo
                    }
                    pCur?.close()
                }
            }
        }
        hashMap.forEach {
            Log.d("asasas",it.key+"    "+ it.value)
        }
        cur?.close()
    }*/
}