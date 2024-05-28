package com.ntu.moulsocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList;
    private final OnPostInteractionListener listener;

    public PostAdapter(List<Post> postList, OnPostInteractionListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.username.setText("Username");
        holder.postTime.setText("2 hours ago");
        holder.postContent.setText(post.getContent());

        if (post.getImageUri() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            Picasso.get().load(post.getImageUri()).into(holder.postImage);
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        holder.likeButton.setImageResource(post.isLiked() ? R.drawable.ic_liked : R.drawable.ic_like);
        holder.likeCount.setText(String.valueOf(post.getLikeCount()));

        holder.likeButton.setOnClickListener(v -> listener.onLikeClicked(position));
        holder.commentButton.setOnClickListener(v -> listener.onCommentClicked(position));
        holder.shareButton.setOnClickListener(v -> listener.onShareClicked(position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage, postImage;
        TextView username, postTime, postContent, likeCount;
        ImageButton likeButton, commentButton, shareButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            postImage = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            postTime = itemView.findViewById(R.id.post_time);
            postContent = itemView.findViewById(R.id.post_content);
            likeCount = itemView.findViewById(R.id.like_count);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }

    public interface OnPostInteractionListener {
        void onLikeClicked(int position);
        void onCommentClicked(int position);
        void onShareClicked(int position);
    }
}
