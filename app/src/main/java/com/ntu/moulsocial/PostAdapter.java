package com.ntu.moulsocial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

        holder.textViewContent.setText(post.getContent());
        holder.textViewLikeCount.setText(String.valueOf(post.getLikeCount()));
        holder.imageButtonLike.setImageResource(post.isLiked() ? R.drawable.ic_liked : R.drawable.ic_like);

        holder.imageButtonLike.setOnClickListener(v -> listener.onLikeClicked(position));
        holder.imageButtonComment.setOnClickListener(v -> listener.onCommentClicked(position));
        holder.imageButtonShare.setOnClickListener(v -> listener.onShareClicked(position));

        if (post.getImageUri() != null) {
            try {
                Uri imageUri = Uri.parse(post.getImageUri());
                InputStream inputStream = holder.itemView.getContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.imageViewPostImage.setImageBitmap(bitmap);
                holder.imageViewPostImage.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                holder.imageViewPostImage.setVisibility(View.GONE);
            }
        } else {
            holder.imageViewPostImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent, textViewLikeCount;
        ImageButton imageButtonLike, imageButtonComment, imageButtonShare;
        ImageView imageViewPostImage;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewPostContent);
            textViewLikeCount = itemView.findViewById(R.id.textViewLikeCount);
            imageButtonLike = itemView.findViewById(R.id.imageButtonLike);
            imageButtonComment = itemView.findViewById(R.id.imageButtonComment);
            imageButtonShare = itemView.findViewById(R.id.imageButtonShare);
            imageViewPostImage = itemView.findViewById(R.id.imageViewPostImage);
        }
    }

    public interface OnPostInteractionListener {
        void onLikeClicked(int position);
        void onCommentClicked(int position);
        void onShareClicked(int position);
    }
}
