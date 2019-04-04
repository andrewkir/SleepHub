package com.andrewkir.sleepproject.Services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.andrewkir.sleepproject.Utilities.LOGIN_URL
import com.andrewkir.sleepproject.Utilities.REGISTER_URL
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

object Web {


    fun login(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val loginBody = jsonBody.toString()
        val loginRequests =
            object : JsonObjectRequest(Request.Method.POST, LOGIN_URL, null, Response.Listener { response ->
                complete(true)
                Log.d("RES", response.toString())
                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not login user: $error")
                complete(false)
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getBody(): ByteArray {
                    return loginBody.toByteArray()
                }
            }
        Volley.newRequestQueue(context).add(loginRequests)
    }

    fun register(context: Context, name: String, username: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("password", password)
        jsonBody.put("username", username)
        val loginBody = jsonBody.toString()
        val loginRequests = object : StringRequest(Request.Method.POST, REGISTER_URL, Response.Listener { response ->
            complete(true)
            Log.d("RES", response.toString())
            Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not login user: $error")
            complete(false)
        }) {
            override fun getBody(): ByteArray {
                return loginBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(loginRequests)
    }


}