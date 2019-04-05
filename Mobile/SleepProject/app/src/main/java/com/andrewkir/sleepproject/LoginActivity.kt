package com.andrewkir.sleepproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import com.andrewkir.sleepproject.R
import com.andrewkir.sleepproject.Services.Web
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enableSpinner(false)
        if (App.prefs.isLoggedIn){
            startActivity(Intent(this, PostsMainActivity::class.java))
            finish()
        } else {
            loginBtn.setOnClickListener {
                val username = editUsernameLogin.text.toString()
                val password = editPasswordLogin.text.toString()
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    enableSpinner(true)
                    Web.login(this, username, createHash(password)) { succes ->
                        if (succes) {
                            startActivity(Intent(this,PostsMainActivity::class.java))
                            finish()
                        }
                        enableSpinner(false)
                    }
                } else {
                    if (username.isEmpty()) editUsernameLogin.error = ""
                    if (password.isEmpty()) editPasswordLogin.error = ""
                }
                startActivity(Intent(this, PostsMainActivity::class.java))
            }
        }


    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginBtn.isEnabled = !enable
        regBtn.isEnabled = !enable
        editUsernameLogin.isEnabled = !enable
        editPasswordLogin.isEnabled = !enable
    }


    fun OnSignClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun createHash(string: String): String {
        val key = "secret hash"
        val keySpec = SecretKeySpec(key.toByteArray(), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(keySpec)
        val sign = mac.doFinal(string.toByteArray())
            .joinToString("") { String.format("%02x", it and 255.toByte()) }
        return sign
    }
}