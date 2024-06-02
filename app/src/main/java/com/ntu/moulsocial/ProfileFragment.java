package com.ntu.moulsocial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;  // Thêm Log để kiểm tra
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class ProfileFragment extends Fragment implements PostAdapter.OnPostInteractionListener {

    private static final String TAG = "ProfileFragment";  // Thêm TAG để log
    private ImageView imageViewProfilePicture;
    private TextView textViewName;
    private EditText editTextName;
    private Button buttonSaveName;
    private RecyclerView recyclerViewPosts;
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private PostAdapter postAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);
        textViewName = view.findViewById(R.id.textViewName);
        editTextName = view.findViewById(R.id.editTextName);
        buttonSaveName = view.findViewById(R.id.buttonSaveName);
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseHelper = new DatabaseHelper(getContext());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            currentUser = databaseHelper.getUser(firebaseUser.getUid());
            if (currentUser != null) {
                updateProfileUI();
                loadPosts();
            } else {
                Log.e(TAG, "User not found in database.");
            }
        } else {
            Log.e(TAG, "FirebaseUser is null.");
        }

        buttonSaveName.setOnClickListener(v -> saveUserName());

        imageViewProfilePicture.setOnClickListener(v -> pickImageFromGallery());

        return view;
    }

    private void updateProfileUI() {
        if (currentUser != null) {
            textViewName.setText(currentUser.getUserName());
            Glide.with(this).load(currentUser.getAvatarUri()).into(imageViewProfilePicture);
        } else {
            Log.e(TAG, "currentUser is null in updateProfileUI.");
        }
    }

    private void saveUserName() {
        String newUserName = editTextName.getText().toString();
        if (!newUserName.isEmpty() && currentUser != null) {
            currentUser.setUserName(newUserName);
            databaseHelper.updateUser(currentUser);
            textViewName.setText(newUserName);
            editTextName.setVisibility(View.GONE);
            buttonSaveName.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "newUserName is empty or currentUser is null in saveUserName.");
        }
    }

    private void loadPosts() {
        List<Post> posts = databaseHelper.getAllPosts();
        postAdapter = new PostAdapter(posts, this);
        recyclerViewPosts.setAdapter(postAdapter);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null && currentUser != null) {
                Glide.with(this).load(selectedImageUri).into(imageViewProfilePicture);
                currentUser.setAvatarUri(selectedImageUri.toString());
                databaseHelper.updateUser(currentUser);
            } else {
                Log.e(TAG, "selectedImageUri or currentUser is null in onActivityResult.");
            }
        } else {
            Log.e(TAG, "Activity result failed or data is null in onActivityResult.");
        }
    }

    @Override
    public void onLikeClicked(int position) {
        // Handle like click
        Post post = postAdapter.getPostAtPosition(position);
        post.setLikes(post.getLikes() + 1);
        databaseHelper.updatePost(post);
        postAdapter.notifyItemChanged(position);
    }

    @Override
    public void onCommentClicked(int position) {
        // Handle comment click
    }

    @Override
    public void onShareClicked(int position) {
        // Handle share click
    }
}
