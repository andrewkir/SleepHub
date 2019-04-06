package com.andrewkir.sleepproject.Services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.andrewkir.sleepproject.App
import com.andrewkir.sleepproject.Utilities.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

object Web {


    fun login(context: Context, username: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("username", username)
        jsonBody.put("password", password)
        val loginBody = jsonBody.toString()
        val loginRequests =
            object : JsonObjectRequest(Request.Method.POST, LOGIN_URL, null, Response.Listener { response ->
                complete(true)
                Log.d("RES", response.toString())
                App.prefs.userToken = response.getString("androidToken")
                App.prefs.userUsername = username
                App.prefs.userName = response.getString("name")
                App.prefs.isLoggedIn = true
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
        val loginRequests =
            object : JsonObjectRequest(Request.Method.POST, REGISTER_URL, null, Response.Listener { response ->
                complete(true)
                Log.d("RES", response.toString())
                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                Toast.makeText(context, response.getString("androidToken"), Toast.LENGTH_LONG).show()

                App.prefs.userToken = response.getString("androidToken")
                App.prefs.userUsername = username
                App.prefs.userName = name
                App.prefs.isLoggedIn = true

            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not login user: $error")
                complete(false)
            }) {
                override fun getBody(): ByteArray {
                    return loginBody.toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }
        Volley.newRequestQueue(context).add(loginRequests)
    }

    fun getPosts(context: Context, token: String, amount: Int, posts: (MutableList<PostMinified>) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("androidToken", token)
        jsonBody.put("amount", amount)
        val loginBody = jsonBody.toString()
        val resPosts = mutableListOf<PostMinified>()
        val loginRequests =
            object : JsonArrayRequest(Request.Method.POST, GET_POSTS, null, Response.Listener { response ->
                for (i in 0 until response.length()) {
                    val obj: JSONObject = response[i] as JSONObject
                    val post = PostMinified(
                        obj.getString("username"),
                        obj.getString("body"),
                        obj.getBoolean("isLiked"),
                        obj.getInt("likes"),
                        obj.getString("id"),
                        obj.getString("date"),
                        obj.getInt("comments")
                    )
                    resPosts.add(post)
                }
                posts(resPosts)
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not login user: $error")
            }) {
                override fun getBody(): ByteArray {
                    return loginBody.toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            }
        Volley.newRequestQueue(context).add(loginRequests)
    }

    fun getPost(context: Context, token: String, id: String, posts: (Post) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("androidToken", token)
        jsonBody.put("id", id)
        val loginBody = jsonBody.toString()
        val loginRequests = object : JsonObjectRequest(Request.Method.POST, GET_POST, null, Response.Listener { obj ->
            val post = Post(
                obj.getInt("likes"),
                obj.getInt("commentsLen"),
                obj.getString("body"),
                obj.getString("username"),
                obj.getBoolean("isMine"),
                obj.getString("date"),
                obj.getBoolean("isLiked")
            )
            posts(post)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not login user: $error")
        }) {
            override fun getBody(): ByteArray {
                return loginBody.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        Volley.newRequestQueue(context).add(loginRequests)
    }
    fun toggleLike(context: Context, token: String, id: String, likeClass: (likeClass) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("androidToken", token)
        jsonBody.put("id", id)
        val loginBody = jsonBody.toString()
        val loginRequests = object : JsonObjectRequest(Request.Method.POST, TOGGLE_LIKE, null, Response.Listener { obj ->
            val post = likeClass(
                obj.getInt("amount"),
                obj.getBoolean("isLiked")
            )
            likeClass(post)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not login user: $error")
        }) {
            override fun getBody(): ByteArray {
                return loginBody.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        Volley.newRequestQueue(context).add(loginRequests)
    }

//    token, amount


}