package com.ntu.moulsocial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private RecyclerView recyclerViewPosts;
    private SharedPreferences sharedPreferences;
    private EditText editTextPostContent;
    private ImageView imageViewSelectedImage;
    private Uri selectedImageUri;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        editTextPostContent = view.findViewById(R.id.editTextPostContent);
        imageViewSelectedImage = view.findViewById(R.id.imageViewSelectedImage);

        Button buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        Button buttonPost = view.findViewById(R.id.buttonCreatePost);

        buttonSelectImage.setOnClickListener(v -> openGallery());
        buttonPost.setOnClickListener(v -> createPost());

        loadPosts();

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void createPost() {
        String content = editTextPostContent.getText().toString();
        String userId = sharedPreferences.getString("user_id", "");

        if (!content.isEmpty() && selectedImageUri != null) {
            String postId = UUID.randomUUID().toString();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("post_images/" + postId);

            imageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    Post post = new Post(postId, userId, content, imageUrl, 0, System.currentTimeMillis());

                    FirebaseUtils.addPost(post);

                    postList.add(0, post);
                    postAdapter.notifyItemInserted(0);
                    recyclerViewPosts.scrollToPosition(0);

                    editTextPostContent.setText("");
                    imageViewSelectedImage.setImageURI(null);
                    selectedImageUri = null;
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to get image URL", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(getContext(), "Please enter content and select an image", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadPosts() {
        FirebaseUtils.loadPosts(posts -> {
            postList = posts != null ? posts : new ArrayList<>();
            postAdapter = new PostAdapter(getContext(), postList, new PostAdapter.OnPostInteractionListener() {
                @Override
                public void onLikeClicked(int position) {
                    Post post = postList.get(position);
                    // Handle like click, e.g. update like count in Firebase
                    FirebaseUtils.updateLikes(post.getId(), post.getLikes());
                }

                @Override
                public void onCommentClicked(int position) {
                    Post post = postList.get(position);
                    CommentDialog commentDialog = new CommentDialog(getContext(), post.getId());
                    commentDialog.show();
                }

                @Override
                public void onShareClicked(int position) {
                    Post post = postList.get(position);
                    // Handle share click, e.g. share post content via intent
                }
            });
            recyclerViewPosts.setAdapter(postAdapter);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewSelectedImage.setImageURI(selectedImageUri);
        }
    }
}
