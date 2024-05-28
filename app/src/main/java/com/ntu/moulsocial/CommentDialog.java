package com.ntu.moulsocial;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class CommentDialog extends Dialog {

    private EditText editTextComment;
    private Button buttonAddComment;
    private CommentDialogListener listener;

    public CommentDialog(@NonNull Context context, CommentDialogListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);

        editTextComment = findViewById(R.id.editTextComment);
        buttonAddComment = findViewById(R.id.buttonAddComment);

        buttonAddComment.setOnClickListener(v -> {
            String comment = editTextComment.getText().toString().trim();
            if (!TextUtils.isEmpty(comment)) {
                listener.onCommentAdded(comment);
                dismiss();
            }
        });
    }

    public interface CommentDialogListener {
        void onCommentAdded(String comment);
    }
}
