package com.ntu.moulsocial;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<Post> filteredPostList;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        RecyclerView recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults);

        postList = new ArrayList<>();
        filteredPostList = new ArrayList<>();

        postAdapter = new PostAdapter(filteredPostList, new PostAdapter.OnPostInteractionListener() {
            @Override
            public void onLikeClicked(int position) {
                // Handle like click
            }

            @Override
            public void onCommentClicked(int position) {
                // Handle comment click
            }

            @Override
            public void onShareClicked(int position) {
                // Handle share click
            }
        });
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearchResults.setAdapter(postAdapter);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");

        // Fetch posts from Firebase
        fetchPostsFromFirebase();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterPosts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }

    private void fetchPostsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postList.add(post);
                }
                filterPosts(""); // Initialize the filter to show all posts
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void filterPosts(String query) {
        filteredPostList.clear();
        if (query.isEmpty()) {
            filteredPostList.addAll(postList);
        } else {
            for (Post post : postList) {
                if (post.getContent().toLowerCase().contains(query.toLowerCase())) {
                    filteredPostList.add(post);
                }
            }
        }
        postAdapter.notifyDataSetChanged();
    }
}
