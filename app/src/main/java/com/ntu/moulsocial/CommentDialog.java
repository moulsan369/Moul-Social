package com.ntu.moulsocial;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


import android.view.Window;
import android.view.WindowManager;


public class CommentDialog extends Dialog {

    private final List<String> comments;
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerViewComments;
    private EditText editTextNewComment;
    private TextView textCommentCount;
    private final ItemPost post;

    public CommentDialog(@NonNull Context context, ItemPost post) {
        super(context);
        this.post = post;
        this.comments = post.getComments();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_comment);

        // Make dialog full-screen
        if (getWindow() != null) {
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }

        textCommentCount = findViewById(R.id.textCommentCount);
        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        editTextNewComment = findViewById(R.id.editTextNewComment);
        Button buttonAddComment = findViewById(R.id.buttonAddComment);

        textCommentCount.setText(String.format("Comments (%d)", comments.size()));

        recyclerViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(comments);
        recyclerViewComments.setAdapter(commentAdapter);

        buttonAddComment.setOnClickListener(v -> {
            String newComment = editTextNewComment.getText().toString().trim();
            if (!newComment.isEmpty()) {
                post.addComment(newComment);
                commentAdapter.notifyItemInserted(comments.size() - 1);
                recyclerViewComments.scrollToPosition(comments.size() - 1); // Scroll to the new comment
                editTextNewComment.setText("");
                textCommentCount.setText(String.format("Comments (%d)", comments.size()));
            }
        });
    }
}
