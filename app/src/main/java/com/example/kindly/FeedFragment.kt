package com.example.kindly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.backend.Post
import com.example.kindly.backend.PostUserAdapter
import com.google.firebase.database.*

class FeedFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var postAdapter: PostUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false) // Use the layout from CharitiesFragment
        val rvPosts: RecyclerView = view.findViewById(R.id.rvFeed) // Make sure to update this to your actual RecyclerView ID
        rvPosts.layoutManager = LinearLayoutManager(requireContext())
        postAdapter = PostUserAdapter(ArrayList())
        rvPosts.adapter = postAdapter

        database = FirebaseDatabase.getInstance()
        val postsRef = database.reference.child("posts") // Update this to match your actual Firebase structure

        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = ArrayList<Post>()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    if (post != null) {
                        postList.add(post)
                    }
                }

                postAdapter.updateData(postList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })

        return view
    }
}
