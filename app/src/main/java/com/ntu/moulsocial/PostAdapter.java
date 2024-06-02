package com.ntu.moulsocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private OnPostInteractionListener listener;

    public interface OnPostInteractionListener {
        void onLikeClicked(int position);
        void onCommentClicked(int position);
        void onShareClicked(int position);
    }

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
        holder.textViewLikes.setText(String.valueOf(post.getLikes()));

        if (post.isLiked()) {
            holder.imageViewLike.setImageResource(R.drawable.ic_liked);
        } else {
            holder.imageViewLike.setImageResource(R.drawable.ic_like);
        }

        holder.imageViewLike.setOnClickListener(v -> {
            if (!post.isLiked()) {
                post.setLiked(true);
                post.incrementLikes();
                holder.imageViewLike.setImageResource(R.drawable.ic_liked);
            } else {
                post.setLiked(false);
                post.setLikes(post.getLikes() - 1);
                holder.imageViewLike.setImageResource(R.drawable.ic_like);
            }
            holder.textViewLikes.setText(String.valueOf(post.getLikes()));
            listener.onLikeClicked(position);
        });

        holder.imageViewComment.setOnClickListener(v -> {
            if (holder.commentSection.getVisibility() == View.GONE) {
                holder.commentSection.setVisibility(View.VISIBLE);
            } else {
                holder.commentSection.setVisibility(View.GONE);
            }
            listener.onCommentClicked(position);
        });

        holder.imageViewShare.setOnClickListener(v -> listener.onShareClicked(position));

        holder.buttonAddComment.setOnClickListener(v -> {
            String comment = holder.editTextAddComment.getText().toString();
            if (!comment.isEmpty()) {
                post.addComment(comment);
                holder.editTextAddComment.setText("");
                notifyItemChanged(position);
            }
        });

        // Load image using Glide
        if (!post.getImageUri().isEmpty()) {
            Glide.with(holder.imageViewPostImage.getContext())
                    .load(post.getImageUri())
                    .into(holder.imageViewPostImage);
            holder.imageViewPostImage.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewPostImage.setVisibility(View.GONE);
        }

        // Set up RecyclerView for comments
        CommentAdapter commentAdapter = new CommentAdapter(post.getComments());
        holder.recyclerViewComments.setAdapter(commentAdapter);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public Post getPostAtPosition(int position) {
        return postList.get(position);
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent;
        TextView textViewLikes;
        ImageView imageViewLike;
        ImageView imageViewComment;
        ImageView imageViewShare;
        ImageView imageViewPostImage;
        LinearLayout commentSection;
        RecyclerView recyclerViewComments;
        EditText editTextAddComment;
        ImageButton buttonAddComment;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewLikes = itemView.findViewById(R.id.textViewLikeCount);
            imageViewLike = itemView.findViewById(R.id.imageButtonLike);
            imageViewComment = itemView.findViewById(R.id.imageButtonComment);
            imageViewShare = itemView.findViewById(R.id.imageButtonShare);
            imageViewPostImage = itemView.findViewById(R.id.imageViewPostImage);
            commentSection = itemView.findViewById(R.id.commentSection);
            recyclerViewComments = itemView.findViewById(R.id.recyclerViewComments);
            editTextAddComment = itemView.findViewById(R.id.editTextAddComment);
            buttonAddComment = itemView.findViewById(R.id.buttonAddComment);
        }
    }
}
