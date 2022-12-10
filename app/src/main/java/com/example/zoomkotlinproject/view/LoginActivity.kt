package com.example.zoomkotlinproject.view

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.zoomkotlinproject.R
import com.example.zoomkotlinproject.databinding.ActivityLoginBinding
import com.example.zoomkotlinproject.utils.Constants
import com.example.zoomkotlinproject.utils.SharedPref
import com.example.zoomkotlinproject.viewmodel.MainViewModel
import com.example.zoomkotlinproject.viewstate.MainViewEvent
import com.example.zoomkotlinproject.viewstate.MainViewState
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var viewModel: MainViewModel
    private var fcmToken: String? = null

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        supportActionBar?.hide()
        val userName: String = SharedPref.readPrefString(this, Constants.LOGIN_ID).toString()
        if (userName.isNotBlank()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        mBinding.lottieView.setAnimation(R.raw.basket_ball_playing)
        mBinding.lottieView.playAnimation()
        val myAnimation: ArrayList<Int> = arrayListOf()
        myAnimation.add(R.raw.football_team_players)
        myAnimation.add(R.raw.stadium)
        myAnimation.add(R.raw.wicket_cricket)
        myAnimation.add(R.raw.basket_ball_playing)
        var animationNumber = 0
        mBinding.lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
//                mBinding.lottieView.pauseAnimation()
                mBinding.lottieView.setAnimation(myAnimation[animationNumber])
                mBinding.lottieView.playAnimation()
                if (animationNumber < myAnimation.size - 1) {
                    animationNumber++
                } else {
                    animationNumber = 0
                }

            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}

        })
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("asasas", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                fcmToken = task.result
            })

        val password: String = SharedPref.readPrefString(this, Constants.LOGIN_PASSWORD).toString()
        if (userName.isNotEmpty()) {
            mBinding.userName.setText(userName)
        }
        if (password.isNotEmpty()) {
            mBinding.password.setText(password)
        }
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.viewState.observe(this) { render(it) }
        mBinding.loginLayout.setOnClickListener {
            mBinding.loginLayout.isEnabled = false
            Constants.hideKeyboard(this)
            when {
                mBinding.userName.text.toString().isEmpty() -> {
                    showSnackBar(getString(R.string.please_enter_user_name))
                    mBinding.userName.error = getString(R.string.please_enter_user_name)
                }
                mBinding.password.text.toString().isEmpty() -> {
                    showSnackBar(getString(R.string.please_enter_password))
                    mBinding.password.error = getString(R.string.please_enter_password)
                }
                else -> {
                    if (Constants.isOnline(this)) {
                        if (fcmToken == null) {
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        Log.w(
                                            "asasas",
                                            "Fetching FCM registration token failed",
                                            task.exception
                                        )
                                        return@OnCompleteListener
                                    }
                                    fcmToken = task.result
                                })
                        }
                        Log.d("asasas==>Token", fcmToken.toString())
                        val deviceId = Settings.Secure.getString(
                            contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                        mBinding.progress.visibility = View.VISIBLE
                        viewModel.onEvent(
                            MainViewEvent.LoginEvent(
                                mBinding.userName.text.toString(),
                                mBinding.password.text.toString(),
                                fcmToken = fcmToken.toString(),
                                deviceId = deviceId
                            )
                        )
                    } else {
                        showSnackBar(getString(R.string.you_are_offline_please_connect_to_internet))
                    }
                }
            }
        }
    }

    private fun render(viewState: MainViewState?) {
        if (viewState == null) return

        viewState.loginResponse?.let {
            mBinding.progress.visibility = View.GONE
            SharedPref.writePrefString(this, Constants.LOGIN_ID, mBinding.userName.text.toString())
            SharedPref.writePrefString(
                this,
                Constants.LOGIN_PASSWORD,
                mBinding.password.text.toString()
            )
            it.data?.user?.userName?.let { it1 ->
                SharedPref.writePrefString(
                    this, Constants.USER_NAME,
                    it1
                )
            }
            it.data?.token?.let { it1 ->
                SharedPref.writePrefString(
                    this, Constants.TOKEN,
                    it1
                )
            }
            SharedPref.writePrefString(this, Constants.APP_KEY, it.data?.appKey ?: "")
            SharedPref.writePrefString(this, Constants.APP_SECRET, it.data?.appSecret ?: "")
            SharedPref.writePrefInt(this, Constants.MIN_APP_VERSION, it.data?.min_apk_version ?: 0)
            SharedPref.writePrefInt(
                this,
                Constants.LATEST_APP_VERSION,
                it.data?.latest_apk_version ?: 0
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constants.IS_NOW_LOGGED_IN, true)
            startActivity(intent)
            finish()
        }

        viewState.error?.let {
            mBinding.progress.visibility = View.GONE
            mBinding.loginLayout.isEnabled = true
            showSnackBar(it)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}