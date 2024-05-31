package com.ntu.moulsocial;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CommentDialog extends Dialog {
    private EditText editTextComment;
    private Button buttonSubmit;
    private OnCommentSubmitListener listener;

    public interface OnCommentSubmitListener {
        void onCommentSubmit(String comment);
    }

    public CommentDialog(Context context, OnCommentSubmitListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);

        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(v -> {
            String comment = editTextComment.getText().toString().trim();
            if (!comment.isEmpty()) {
                listener.onCommentSubmit(comment);
                dismiss();
            }
        });
    }
}
