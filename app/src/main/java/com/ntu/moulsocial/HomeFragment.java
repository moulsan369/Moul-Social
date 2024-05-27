package com.ntu.moulsocial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PostAdapter.OnPostInteractionListener {

    private PostAdapter postAdapter;
    private List<ItemPost> postList;
    private EditText editTextPostContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        editTextPostContent = view.findViewById(R.id.editTextPostContent);
        Button buttonCreatePost = view.findViewById(R.id.buttonCreatePost);

        // Initialize post list
        postList = new ArrayList<>();

        // Add a sample post
        postList.add(new ItemPost(
                "John Doe",
                "2 hours ago",
                "This is a sample post content.",
                R.drawable.profile_picture_background,
                R.drawable.post_image_placeholder
        ));

        // Set up RecyclerView and adapter
        postAdapter = new PostAdapter(getContext(), postList, this);
        recyclerView.setAdapter(postAdapter);

        // Set up button click listener
        buttonCreatePost.setOnClickListener(v -> createNewPost());

        return view;
    }

    // Method to create a new post
    private void createNewPost() {
        String content = editTextPostContent.getText().toString();
        if (!content.isEmpty()) {
            postList.add(new ItemPost(
                    "New User",
                    "Just now",
                    content,
                    R.drawable.profile_picture_background,
                    R.drawable.post_image_placeholder // You can change this
            ));
            postAdapter.notifyItemInserted(postList.size() - 1);
            editTextPostContent.setText(""); // Clear the input field
        }
    }

    @Override
    public void onCommentClicked(ItemPost post) {
        // Handle the comment click event
        Toast.makeText(getContext(), "Comment clicked on post: " + post.getContent(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLikeClicked(ItemPost post) {
        // Handle the like click event
        Toast.makeText(getContext(), "Like clicked on post: " + post.getContent(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareClicked(ItemPost post) {
        // Handle the share click event
        Toast.makeText(getContext(), "Share clicked on post: " + post.getContent(), Toast.LENGTH_SHORT).show();
    }
}
