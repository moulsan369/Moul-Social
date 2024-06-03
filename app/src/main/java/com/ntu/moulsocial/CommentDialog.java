package com.ntu.moulsocial;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CommentDialog extends Dialog {

    private String postId;
    private DatabaseReference commentsRef;
    private EditText editTextComment;
    private Button buttonSubmit;

    public CommentDialog(@NonNull Context context, String postId) {
        super(context);
        this.postId = postId;
        this.commentsRef = FirebaseDatabase.getInstance().getReference("comments");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);

        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(v -> {
            String commentContent = editTextComment.getText().toString().trim();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null && !commentContent.isEmpty()) {
                String userId = currentUser.getUid();
                String profilePictureUrl = currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "";
                Comment comment = new Comment(UUID.randomUUID().toString(), postId, userId, commentContent, profilePictureUrl);

                addCommentToFirebase(comment);
                dismiss();
            } else {
                if (currentUser == null) {
                    // Handle the case when currentUser is null
                }
                if (commentContent.isEmpty()) {
                    // Handle the case when commentContent is empty
                }
            }
        });

    }

    private void addCommentToFirebase(Comment comment) {
        commentsRef.child(postId).child(comment.getId()).setValue(comment)
                .addOnSuccessListener(aVoid -> {
                    // Log success or handle comment-addition success
                })
                .addOnFailureListener(e -> {
                    // Log or handle the error
                });
    }
}
