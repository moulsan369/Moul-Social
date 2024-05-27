package com.ntu.moulsocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<Post> posts;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.username.setText(post.getUsername());
        holder.postTime.setText(post.getPostTime());
        holder.postContent.setText(post.getPostContent());

        // Set up image visibility
        if (post.getPostImage() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            holder.postImage.setImageBitmap(post.getPostImage());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        holder.likeButton.setOnClickListener(v -> {
            post.incrementLikeCount();
            holder.likeCount.setText(post.getLikeCount() + " likes");
        });

        holder.commentButton.setOnClickListener(v -> {
            // Handle comment button click
        });

        holder.shareButton.setOnClickListener(v -> {
            // Handle share button click
        });

        holder.likeCount.setText(post.getLikeCount() + " likes");
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView username, postTime, postContent, likeCount;
        ImageView postImage, profileImage;
        ImageButton likeButton, commentButton, shareButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            postTime = itemView.findViewById(R.id.post_time);
            postContent = itemView.findViewById(R.id.post_content);
            postImage = itemView.findViewById(R.id.post_image);
            profileImage = itemView.findViewById(R.id.profile_image);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            shareButton = itemView.findViewById(R.id.share_button);
            likeCount = itemView.findViewById(R.id.textLikeCount);
        }
    }
}
