package com.ntu.moulsocial;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class FirebaseUtils {

    private static final DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
    private static final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
    private static final DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("comments");
    private static final DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications");

    public interface OnDataLoadedListener<T> {
        void onDataLoaded(T data);
    }

    public static void addPost(Post post) {
        postsRef.child(post.getId()).setValue(post)
                .addOnSuccessListener(aVoid -> {
                    // Log success or handle post-addition success
                })
                .addOnFailureListener(e -> {
                    // Log or handle the error
                });
    }

    public static void loadPosts(OnDataLoadedListener<List<Post>> listener) {
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> posts = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }
                listener.onDataLoaded(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onDataLoaded(null);
            }
        });
    }

    public static void updateLikes(String postId, int likes) {
        postsRef.child(postId).child("likes").setValue(likes)
                .addOnSuccessListener(aVoid -> {
                    // Log success or handle like-update success
                })
                .addOnFailureListener(e -> {
                    // Log or handle the error
                });
    }

    public static void loadUser(String userId, OnDataLoadedListener<User> listener) {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                listener.onDataLoaded(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onDataLoaded(null);
            }
        });
    }

    public static void loadComments(String postId, OnDataLoadedListener<List<Comment>> listener) {
        commentsRef.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    if (comment != null) {
                        comments.add(comment);
                    }
                }
                listener.onDataLoaded(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onDataLoaded(null);
            }
        });
    }

    public static void addComment(Comment comment, String postId) {
        commentsRef.child(postId).child(comment.getId()).setValue(comment)
                .addOnSuccessListener(aVoid -> {
                    // Log success or handle comment-addition success
                })
                .addOnFailureListener(e -> {
                    // Log or handle the error
                });
    }

    public static DatabaseReference getNotificationsReference() {
        return notificationsRef;
    }

    public static DatabaseReference getUserReference(String userId) {
        return usersRef.child(userId);
    }
}
