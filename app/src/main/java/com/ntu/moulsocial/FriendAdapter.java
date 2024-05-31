package com.ntu.moulsocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friendList;

    public FriendAdapter(List<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        holder.bind(friendList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewProfilePicture;
        private TextView textViewName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfilePicture = itemView.findViewById(R.id.imageViewProfilePicture);
            textViewName = itemView.findViewById(R.id.textViewName);
        }

        public void bind(Friend friend) {
            textViewName.setText(friend.getName());
            if (friend.getProfilePictureUri() != null) {
                Glide.with(imageViewProfilePicture.getContext())
                        .load(friend.getProfilePictureUri())
                        .into(imageViewProfilePicture);
            } else {
                imageViewProfilePicture.setImageResource(R.drawable.profile_picture_placeholder);
            }
        }
    }
}
