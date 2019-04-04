package com.andrewkir.sleepproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import com.andrewkir.sleepproject.R
import com.andrewkir.sleepproject.Services.Web


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enableSpinner(false)
        loginBtn.setOnClickListener {
            val name = editName.text.toString()
            val password = editPassword.text.toString()
            if (name.isNotEmpty() && password.isNotEmpty()) {
                enableSpinner(true)
                Web.login(this, name, password) { succes ->
                    if (succes) {
                        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
                    }
                    enableSpinner(false)
                }
            } else {
                if (name.isEmpty()) editName.error = ""
                if (password.isEmpty()) editPassword.error = ""
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
        editName.isEnabled = !enable
        editPassword.isEnabled = !enable
    }


    fun OnSignClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}