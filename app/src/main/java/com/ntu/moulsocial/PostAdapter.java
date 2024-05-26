package com.ntu.moulsocial;

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

    private List<Post> postList;
    private OnPostInteractionListener listener;

    public interface OnPostInteractionListener {
        void onLikeClicked(Post post, int position);
        void onCommentClicked(Post post, int position);
        void onShareClicked(Post post, int position);
    }

    public PostAdapter(List<Post> postList, OnPostInteractionListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.textLikeCount.setText(String.valueOf(post.getLikeCount()));
        if (post.isLiked()) {
            holder.buttonLike.setImageResource(R.drawable.ic_like);
        } else {
            holder.buttonLike.setImageResource(R.drawable.ic_like_outline);
        }

        holder.buttonLike.setOnClickListener(v -> {
            if (post.isLiked()) {
                post.setLiked(false);
                post.decrementLikeCount();
            } else {
                post.setLiked(true);
                post.incrementLikeCount();
            }
            notifyItemChanged(position);
            listener.onLikeClicked(post, position);
        });

        holder.buttonComment.setOnClickListener(v -> listener.onCommentClicked(post, position));

        holder.buttonShare.setOnClickListener(v -> listener.onShareClicked(post, position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView buttonLike;
        public TextView textLikeCount;
        public ImageButton buttonComment;
        public ImageButton buttonShare;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonLike = itemView.findViewById(R.id.like_button);
            textLikeCount = itemView.findViewById(R.id.textLikeCount);
            buttonComment = itemView.findViewById(R.id.comment_button);
            buttonShare = itemView.findViewById(R.id.share_button);
        }
    }
}
