package com.example.kindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class PostManageAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_manage_admin)
    }

    fun addpost(view: View) {
        val intent = Intent(this, AddPost::class.java)
        startActivity(intent)
    }
}