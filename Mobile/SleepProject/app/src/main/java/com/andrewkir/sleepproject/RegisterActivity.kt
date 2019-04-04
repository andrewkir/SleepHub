package com.andrewkir.sleepproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.andrewkir.sleepproject.R
import kotlinx.android.synthetic.main.activity_register.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        enableSpinner(false)
        println("FUCKI")
        println(createHash("asd"))


        registerButton.setOnClickListener {
            val password = editPassword.text.toString()
            val password_confirm = editPassword2.text.toString()
            if(password == password_confirm){
                val name = editName.text.toString()
                val surname = editSurname.text.toString()
                val username = editUsername.text.toString()
                if (password.isEmpty() && password_confirm.isNotBlank() && name.isEmpty() && surname.isNotEmpty() && username.isNotEmpty()){

                } else {
                    if(password.isEmpty()) editPassword.error = ""
                    if(password_confirm.isEmpty()) editPassword2.error = ""
                    if(name.isEmpty()) editName.error = ""
                    if(surname.isEmpty()) editSurname.error = ""
                    if(username.isEmpty()) editUsername.error = ""
                }
            } else {
                Toast.makeText(this, "Please check passwords",Toast.LENGTH_SHORT).show()
                editPassword2.error = ""
                editPassword.error = ""
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
        editName.isEnabled = !enable
        editPassword.isEnabled = !enable
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
