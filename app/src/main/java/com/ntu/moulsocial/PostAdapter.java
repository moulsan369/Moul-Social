package com.ntu.moulsocial;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.UUID;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private static List<Post> postList;
    private OnPostInteractionListener listener;

    public interface OnPostInteractionListener {
        void onLikeClicked(int position);
        void onCommentClicked(int position);
        void onShareClicked(int position);
    }

    public PostAdapter(Context context, List<Post> postList, OnPostInteractionListener listener) {
        this.context = context;
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

        FirebaseUtils.loadUser(post.getUserId(), user -> {
            if (user != null) {
                holder.textViewUserName.setText(user.getName());
                Glide.with(holder.itemView.getContext())
                        .load(user.getPhotoUrl())
                        .into(holder.imageViewProfilePicture);
            }
        });

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
                FirebaseUtils.loadComments(post.getId(), comments -> {
                    post.setComments(comments);
                    holder.recyclerViewComments.setAdapter(new CommentAdapter(comments));
                });
            } else {
                holder.commentSection.setVisibility(View.GONE);
            }
            listener.onCommentClicked(position);
        });

        holder.imageViewShare.setOnClickListener(v -> listener.onShareClicked(position));

        Glide.with(holder.itemView.getContext())
                .load(post.getImageUri())
                .into(holder.imageViewPostImage);
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
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
        ImageView imageViewProfilePicture;
        TextView textViewUserName;

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
            imageViewProfilePicture = itemView.findViewById(R.id.imageViewProfilePicture);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);

            buttonAddComment.setOnClickListener(v -> {
                String commentContent = editTextAddComment.getText().toString();
                if (!commentContent.isEmpty()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = currentUser.getUid();
                    String profilePictureUrl = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "";
                    Comment comment = new Comment(UUID.randomUUID().toString(), postList.get(getAdapterPosition()).getId(), userId, commentContent, profilePictureUrl);

                    FirebaseUtils.addComment(comment, postList.get(getAdapterPosition()).getId());

                    editTextAddComment.setText("");
                    FirebaseUtils.loadComments(postList.get(getAdapterPosition()).getId(), comments -> {
                        postList.get(getAdapterPosition()).setComments(comments);
                        recyclerViewComments.setAdapter(new CommentAdapter(comments));
                    });
                }
            });
        }
    }
}

