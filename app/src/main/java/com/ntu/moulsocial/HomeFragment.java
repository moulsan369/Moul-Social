package com.ntu.moulsocial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PostAdapter.OnPostInteractionListener {

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private EditText editTextPostContent;
    private ImageView imageViewSelectedImage;
    private Uri selectedImageUri;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editTextPostContent = view.findViewById(R.id.editTextPostContent);
        imageViewSelectedImage = view.findViewById(R.id.imageViewSelectedImage);
        RecyclerView recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);

        Button buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        Button buttonCreatePost = view.findViewById(R.id.buttonCreatePost);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPosts.setAdapter(postAdapter);

        buttonSelectImage.setOnClickListener(v -> selectImage());
        buttonCreatePost.setOnClickListener(v -> createPost());

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewSelectedImage.setImageBitmap(bitmap);
                imageViewSelectedImage.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void createPost() {
        String content = editTextPostContent.getText().toString().trim();
        if (TextUtils.isEmpty(content) && selectedImageUri == null) {
            return; // No content to post
        }

        Post post = new Post(content, selectedImageUri != null ? selectedImageUri.toString() : null);
        postList.add(0, post); // Add new post at the top
        postAdapter.notifyItemInserted(0);
        editTextPostContent.setText("");
        imageViewSelectedImage.setVisibility(View.GONE);
        selectedImageUri = null;
    }

    @Override
    public void onLikeClicked(int position) {
        Post post = postList.get(position);
        post.setLiked(!post.isLiked());
        post.setLikeCount(post.isLiked() ? post.getLikeCount() + 1 : post.getLikeCount() - 1);
        postAdapter.notifyItemChanged(position);
    }

    @Override
    public void onCommentClicked(int position) {
        // Handle comment action
    }

    @Override
    public void onShareClicked(int position) {
        // Handle share action
    }
}
