package com.ntu.moulsocial;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.textViewCommentContent.setText(comment.getContent());

        if (comment.getProfilePictureUri() != null) {
            holder.imageViewCommentProfilePicture.setImageURI(Uri.parse(comment.getProfilePictureUri()));
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCommentProfilePicture;
        TextView textViewCommentContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCommentProfilePicture = itemView.findViewById(R.id.imageViewCommentProfilePicture);
            textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
        }
    }
}
