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

    private final List<ItemPost> posts;
    private final Context context;
    private final OnPostInteractionListener listener;

    public interface OnPostInteractionListener {
        void onCommentClicked(ItemPost post);
        void onLikeClicked(ItemPost post);
        void onShareClicked(ItemPost post);
    }

    public PostAdapter(Context context, List<ItemPost> posts, OnPostInteractionListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ItemPost post = posts.get(position);

        holder.usernameTextView.setText(post.getUsername());
        holder.postTimeTextView.setText(post.getPostTime());
        holder.postContentTextView.setText(post.getPostContent());
        holder.profileImageView.setImageResource(post.getProfileImage());
        holder.postImageView.setImageResource(post.getPostImage());

        holder.commentButton.setOnClickListener(v -> listener.onCommentClicked(post));
        holder.likeButton.setOnClickListener(v -> {
            post.setLiked(!post.isLiked());
            notifyItemChanged(position);
            listener.onLikeClicked(post);
        });
        holder.shareButton.setOnClickListener(v -> listener.onShareClicked(post));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView postTimeTextView;
        TextView postContentTextView;
        ImageView profileImageView;
        ImageView postImageView;
        ImageButton commentButton;
        ImageButton likeButton;
        ImageButton shareButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            postTimeTextView = itemView.findViewById(R.id.post_time);
            postContentTextView = itemView.findViewById(R.id.post_content);
            profileImageView = itemView.findViewById(R.id.profile_image);
            postImageView = itemView.findViewById(R.id.post_image);
            commentButton = itemView.findViewById(R.id.comment_button);
            likeButton = itemView.findViewById(R.id.like_button);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }

}
