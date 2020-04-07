package com.llistochek.advanceddiary

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.llistochek.advanceddiary.api.LoginResponse
import com.llistochek.advanceddiary.api.NetworkService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException


class LoginActivity : AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.IO) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Prefs.isUserLoggedIn(this)) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            setContentView(R.layout.activity_login)
            val token = runBlocking(this.coroutineContext) {
                FirebaseAnalytics.getInstance(this@LoginActivity).firebaseInstanceId
            }
            serversSpinner.adapter = ArrayAdapter<String>(this, R.layout.servers_spinner_item, R.id.server, resources.getStringArray(R.array.servers))
            logInButton.setOnClickListener {
                launch {
                    lateinit var dialog: AlertDialog
                    runOnUiThread {
                        dialog = AlertDialog.Builder(this@LoginActivity)
                            .setView(R.layout.auth_loading_alert_dialog)
                            .setCancelable(false)
                            .show()
                    }
                    NetworkService.schoolApi.login(
                        login.text.toString(),
                        password.text.toString(),
                        token, packageName
                    ).enqueue(object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            if (t is UnknownHostException) {
                                snack(getString(R.string.no_internet_connection))
                            } else snack(getString(R.string.request_error, t.message))
                            dialog.dismiss()
                        }

                        override fun onResponse(p0: Call<LoginResponse>, response: Response<LoginResponse>) {
                            val body = response.body()
                            if (body != null) {
                                when {
                                    body.success -> {
                                        Prefs.setStudentInfo(this@LoginActivity, body)
                                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    }
                                    body.message == null -> snack(getString(R.string.incorrect_login_or_password))
                                    else -> AlertDialog.Builder(this@LoginActivity)
                                        .setMessage(body.message)
                                        .setPositiveButton(android.R.string.ok) { d, _ -> d.dismiss() }
                                        .show()
                                }
                                dialog.dismiss()
                            } else snack(getString(R.string.response_error, response.code()))
                        }
                    })
                }
            }
        }
    }

    fun snack(text: String) {
        Snackbar.make(
            loginLayout,
            text,
            3500
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
