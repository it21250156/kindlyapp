package com.example.kindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CharityManageAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charity_manage_admin)
    }

    fun addcharity(view: View) {
        val intent = Intent(this, AddCharity::class.java)
        startActivity(intent)
    }
}