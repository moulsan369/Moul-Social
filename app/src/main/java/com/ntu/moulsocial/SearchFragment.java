package com.ntu.moulsocial;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<Post> filteredPostList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        RecyclerView recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults);

        postList = new ArrayList<>();
        filteredPostList = new ArrayList<>();

        sharedPreferences = getContext().getSharedPreferences("MoulSocialPrefs", Context.MODE_PRIVATE);
        gson = new Gson();

        postAdapter = new PostAdapter(getContext(), filteredPostList, new PostAdapter.OnPostInteractionListener() {
            @Override
            public void onLikeClicked(int position) {
                // Handle like click
            }

            @Override
            public void onCommentClicked(int position) {
                // Handle comment click
            }

            @Override
            public void onShareClicked(int position) {
                // Handle share click
            }
        });

        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearchResults.setAdapter(postAdapter);

        fetchPostsFromSharedPreferences();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterPosts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }

    private void fetchPostsFromSharedPreferences() {
        String postsJson = sharedPreferences.getString("posts", "");
        if (!postsJson.isEmpty()) {
            Type type = new TypeToken<List<Post>>() {}.getType();
            postList = gson.fromJson(postsJson, type);
            filterPosts("");
        }
    }

    private void filterPosts(String query) {
        filteredPostList.clear();
        if (query.isEmpty()) {
            filteredPostList.addAll(postList);
        } else {
            for (Post post : postList) {
                if (post.getContent().toLowerCase().contains(query.toLowerCase())) {
                    filteredPostList.add(post);
                }
            }
        }
        postAdapter.notifyDataSetChanged();
    }
}
