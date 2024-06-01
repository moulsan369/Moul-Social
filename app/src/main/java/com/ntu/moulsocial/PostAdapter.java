package com.ntu.moulsocial;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface OnPostInteractionListener {
        void onLikeClicked(int position);
        void onCommentClicked(int position);
        void onShareClicked(int position);
    }

    private final List<Post> postList;
    private final OnPostInteractionListener listener;

    public PostAdapter(List<Post> postList, OnPostInteractionListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textViewContent.setText(post.getContent());
        holder.textViewLikeCount.setText(String.valueOf(post.getLikeCount()));

        if (post.getImageUri() != null) {
            holder.imageViewPostImage.setVisibility(View.VISIBLE);
            holder.imageViewPostImage.setImageURI(Uri.parse(post.getImageUri()));
        } else {
            holder.imageViewPostImage.setVisibility(View.GONE);
        }

        if (post.getProfilePictureUri() != null) {
            holder.imageViewProfilePicture.setImageURI(Uri.parse(post.getProfilePictureUri()));
        }

        holder.imageButtonLike.setImageResource(post.isLiked() ? R.drawable.ic_liked : R.drawable.ic_like);

        holder.imageButtonLike.setOnClickListener(v -> listener.onLikeClicked(position));
        holder.imageButtonComment.setOnClickListener(v -> {
            holder.commentSection.setVisibility(holder.commentSection.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        holder.imageButtonShare.setOnClickListener(v -> listener.onShareClicked(position));

        holder.recyclerViewComments.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerViewComments.setAdapter(new CommentAdapter(post.getComments()));

        holder.buttonAddComment.setOnClickListener(v -> {
            String commentContent = holder.editTextAddComment.getText().toString().trim();
            if (!commentContent.isEmpty()) {
                Comment newComment = new Comment(commentContent, post.getProfilePictureUri());
                post.addComment(newComment);
                holder.editTextAddComment.setText("");
                holder.recyclerViewComments.getAdapter().notifyDataSetChanged();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent;
        TextView textViewLikeCount;
        ImageView imageViewPostImage;
        ImageView imageViewProfilePicture;
        ImageButton imageButtonLike;
        ImageButton imageButtonComment;
        ImageButton imageButtonShare;
        RecyclerView recyclerViewComments;
        EditText editTextAddComment;
        ImageButton buttonAddComment;
        View commentSection;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewLikeCount = itemView.findViewById(R.id.textViewLikeCount);
            imageViewPostImage = itemView.findViewById(R.id.imageViewPostImage);
            imageViewProfilePicture = itemView.findViewById(R.id.imageViewProfilePicture);
            imageButtonLike = itemView.findViewById(R.id.imageButtonLike);
            imageButtonComment = itemView.findViewById(R.id.imageButtonComment);
            imageButtonShare = itemView.findViewById(R.id.imageButtonShare);
            recyclerViewComments = itemView.findViewById(R.id.recyclerViewComments);
            editTextAddComment = itemView.findViewById(R.id.editTextAddComment);
            buttonAddComment = itemView.findViewById(R.id.buttonAddComment);
            commentSection = itemView.findViewById(R.id.commentSection);
        }
    }
}
