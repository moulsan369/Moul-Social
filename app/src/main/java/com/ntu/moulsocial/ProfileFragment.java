package com.ntu.moulsocial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView userNameTextView;
    private TextView userEmailTextView;
    private ImageView userPhotoImageView;
    private ImageView userCoverPhotoImageView; // Thêm biến này

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userNameTextView = view.findViewById(R.id.textViewName);
        userEmailTextView = view.findViewById(R.id.textViewEmail);
        userPhotoImageView = view.findViewById(R.id.imageViewProfilePicture);
        userCoverPhotoImageView = view.findViewById(R.id.imageViewCoverPhoto); // Ánh xạ biến này

        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getActivity().MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId != null) {
            fetchUserData(userId);
        }

        return view;
    }

    private void fetchUserData(String userId) {
        FirebaseUtils.getUserReference(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    userNameTextView.setText(user.getName());
                    userEmailTextView.setText(user.getEmail());

                    if (user.getPhotoUrl() != null) {
                        Glide.with(getActivity()).load(user.getPhotoUrl()).into(userPhotoImageView);
                    } else {
                        userPhotoImageView.setImageResource(R.drawable.profile_picture_placeholder); // Default avatar if no photo is available
                    }

                    if (user.getCoverPhotoUrl() != null) { // Tải ảnh bìa
                        Glide.with(getActivity()).load(user.getCoverPhotoUrl()).into(userCoverPhotoImageView);
                    } else {
                        userCoverPhotoImageView.setImageResource(R.drawable.cover_photo_placeholder); // Default cover photo if no photo is available
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}
