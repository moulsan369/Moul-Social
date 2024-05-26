package com.ntu.moulsocial;

import android.content.Intent;
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

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
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
        holder.buttonLike.setImageResource(post.isLiked() ? R.drawable.ic_like_filled : R.drawable.ic_like_outline);

        holder.buttonLike.setOnClickListener(v -> {
            if (post.isLiked()) {
                post.setLiked(false);
                post.decrementLikeCount();
            } else {
                post.setLiked(true);
                post.incrementLikeCount();
            }
            notifyItemChanged(position);
        });

        holder.buttonComment.setOnClickListener(v -> {
            // Open comment section or activity/fragment
            // Example:
            // Intent intent = new Intent(v.getContext(), CommentActivity.class);
            // intent.putExtra("post", post);
            // v.getContext().startActivity(intent);
        });

        holder.buttonShare.setOnClickListener(v -> {
            // Share post content
            // Example:
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, post.getContent());
            shareIntent.setType("text/plain");
            v.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
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
