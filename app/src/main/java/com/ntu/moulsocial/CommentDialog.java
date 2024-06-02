package com.ntu.moulsocial;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class CommentDialog extends Dialog {

    private String postId;
    private DatabaseHelper databaseHelper;
    private EditText editTextComment;
    private Button buttonSubmit;

    public CommentDialog(@NonNull Context context, String postId) {
        super(context);
        this.postId = postId;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);

        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = editTextComment.getText().toString();
                if (!commentContent.isEmpty()) {
                    Comment comment = new Comment(UUID.randomUUID().toString(), postId, FirebaseAuth.getInstance().getCurrentUser().getUid(), commentContent);
                    databaseHelper.addComment(comment);
                    dismiss();
                }
            }
        });
    }
}
