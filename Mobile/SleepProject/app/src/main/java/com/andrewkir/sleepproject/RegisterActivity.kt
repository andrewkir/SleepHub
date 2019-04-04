package com.andrewkir.sleepproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.andrewkir.sleepproject.Services.Web
import kotlinx.android.synthetic.main.activity_register.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        enableSpinner(false)
        println(createHash("asd"))


        registerButton.setOnClickListener {
            val password = editPasswordRegister.text.toString()
            val password_confirm = editPassword2Register.text.toString()
            if (password == password_confirm) {
                val name = editNameRegister.text.toString()
                val username = editUsernameRegister.text.toString()
                if (password.isNotEmpty() && password_confirm.isNotEmpty() && name.isNotEmpty() && username.isNotEmpty()){
                    Toast.makeText(this, "sent", Toast.LENGTH_SHORT).show()
                    Web.register(this, name, username, createHash(password)) { succes ->
                        if (succes) {
//                            Toast.makeText(this, "oh yeah", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,PostsMainActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    if(password.isEmpty()) editPasswordRegister.error = ""
                    if(password_confirm.isEmpty()) editPassword2Register.error = ""
                    if(name.isEmpty()) editNameRegister.error = ""
                    if(username.isEmpty()) editUsernameRegister.error = ""
                }
            } else {
                Toast.makeText(this, "Please check passwords", Toast.LENGTH_SHORT).show()
                editPassword2Register.error = ""
                editPasswordRegister.error = ""
            }
        }
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            registerSpinner.visibility = View.VISIBLE
        } else {
            registerSpinner.visibility = View.INVISIBLE
        }
        registerButton.isEnabled = !enable
        editNameRegister.isEnabled = !enable
        editPasswordRegister.isEnabled = !enable
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
