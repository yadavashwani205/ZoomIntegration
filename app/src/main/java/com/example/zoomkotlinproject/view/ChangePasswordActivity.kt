package com.example.zoomkotlinproject.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.zoomkotlinproject.R
import com.example.zoomkotlinproject.databinding.ActivityChangePasswordBinding
import com.example.zoomkotlinproject.utils.Constants
import com.example.zoomkotlinproject.utils.SharedPref
import com.example.zoomkotlinproject.viewmodel.MainViewModel
import com.example.zoomkotlinproject.viewstate.MainViewEvent
import com.example.zoomkotlinproject.viewstate.MainViewState
import com.google.android.material.snackbar.Snackbar

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityChangePasswordBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.viewState.observe(this) { render(it) }
        mBinding.changePasswordLayout.setOnClickListener {
            when {
                mBinding.oldPassword.text.isNullOrBlank() -> {
                    mBinding.oldPassword.error = getString(R.string.please_enter_old_password)
                    showSnackBar(getString(R.string.please_enter_old_password))
                }
                mBinding.newPassword.text.isNullOrBlank() -> {
                    mBinding.newPassword.error = getString(R.string.please_enter_new_password)
                    showSnackBar(getString(R.string.please_enter_new_password))
                }
                mBinding.confirmPassword.text.isNullOrBlank() -> {
                    mBinding.confirmPassword.error =
                        getString(R.string.please_enter_confirm_password)
                    showSnackBar(getString(R.string.please_enter_confirm_password))
                }
                mBinding.confirmPassword.text.toString() != mBinding.newPassword.text.toString() -> {
                    showSnackBar(getString(R.string.new_password_confirm_not_matched))
                }
                else -> {
                    mBinding.progress.visibility = View.VISIBLE
                    viewModel.onEvent(
                        MainViewEvent.ChangePasswordEvent(
                            this,
                            "Bearer ${
                                SharedPref.readPrefString(
                                    this,
                                    Constants.TOKEN
                                )
                            }",
                            mBinding.oldPassword.text.toString(),
                            mBinding.newPassword.text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun render(viewState: MainViewState?) {
        if (viewState == null) return
        viewState.changePasswordResponse?.let {
            mBinding.progress.visibility = View.GONE
            showSnackBar(it.message.toString())
            finish()
        }
        viewState.error?.let {
            mBinding.progress.visibility = View.GONE
            showSnackBar(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}