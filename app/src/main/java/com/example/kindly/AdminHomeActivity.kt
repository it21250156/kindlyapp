package com.example.kindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AdminHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
    }

    fun CharityManagement(view: View) {
        val intent = Intent(this, CharityManageAdmin::class.java)
        startActivity(intent)
    }

    fun PostManagement(view: View) {
        val intent = Intent(this, PostManageAdmin::class.java)
        startActivity(intent)
    }
}