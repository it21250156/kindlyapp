// PostManageAdmin.kt
package com.example.kindly

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kindly.backend.Post
import com.example.kindly.backend.PostAdapter
import com.example.kindly.databinding.ActivityPostManageAdminBinding
import com.google.firebase.database.*

class PostManageAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityPostManageAdminBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostManageAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        val postsRef = database.reference.child("posts")

        postAdapter = PostAdapter(emptyList(), emptyList())
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.rvPosts.adapter = postAdapter

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = mutableListOf<Post>()
                val postKeys = mutableListOf<String>()

                for (postSnapshot in snapshot.children) {
                    val postData = postSnapshot.getValue(Post::class.java)
                    if (postData != null) {
                        postList.add(postData)
                        postKeys.add(postSnapshot.key ?: "")
                    }
                }

                postAdapter.updateData(postList, postKeys)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error if needed
            }
        })
    }

    fun addpost(view: View) {
        val intent = Intent(this, AddPost::class.java)
        startActivity(intent)
    }
}
