package com.ntu.moulsocial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private EditText postContentEditText;
    private Button createPostButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, getContext());
        recyclerView.setAdapter(postAdapter);

        postContentEditText = view.findViewById(R.id.editTextPostContent);
        createPostButton = view.findViewById(R.id.buttonCreatePost);

        createPostButton.setOnClickListener(v -> {
            String content = postContentEditText.getText().toString();
            if (!content.isEmpty()) {
                Post newPost = new Post("Username", "Just now", content, null, 0);
                postList.add(0, newPost);
                postAdapter.notifyItemInserted(0);
                recyclerView.smoothScrollToPosition(0);
                postContentEditText.setText("");
            }
        });

        return view;
    }
}
