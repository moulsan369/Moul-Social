package com.ntu.moulsocial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_CODE_SELECT_IMAGE_COVER = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE_PROFILE = 2;

    private ImageView imageViewCoverPhoto;
    private ImageView imageViewProfilePicture;
    private Uri selectedCoverPhotoUri;
    private Uri selectedProfilePictureUri;

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageViewCoverPhoto = view.findViewById(R.id.imageViewCoverPhoto);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);

        imageViewCoverPhoto.setOnClickListener(v -> selectImage(REQUEST_CODE_SELECT_IMAGE_COVER));
        imageViewProfilePicture.setOnClickListener(v -> selectImage(REQUEST_CODE_SELECT_IMAGE_PROFILE));

        Button buttonAddFriend = view.findViewById(R.id.buttonAddFriend);
        Button buttonFollow = view.findViewById(R.id.buttonFollow);

        buttonAddFriend.setOnClickListener(v -> addFriend());
        buttonFollow.setOnClickListener(v -> follow());

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, new PostAdapter.OnPostInteractionListener() {
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
        });

        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPosts.setAdapter(postAdapter);

        return view;
    }

    private void selectImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                if (requestCode == REQUEST_CODE_SELECT_IMAGE_COVER) {
                    selectedCoverPhotoUri = selectedImageUri;
                    imageViewCoverPhoto.setImageBitmap(bitmap);
                } else if (requestCode == REQUEST_CODE_SELECT_IMAGE_PROFILE) {
                    selectedProfilePictureUri = selectedImageUri;
                    imageViewProfilePicture.setImageBitmap(bitmap);
                    updateProfilePicturesInPosts(selectedProfilePictureUri);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfilePicturesInPosts(Uri profilePictureUri) {
        for (Post post : postList) {
            post.setProfilePictureUri(profilePictureUri.toString());
        }
        postAdapter.notifyDataSetChanged();
    }

    private void addFriend() {
        // Implement add friend logic
    }

    private void follow() {
        // Implement follow logic
    }
}
