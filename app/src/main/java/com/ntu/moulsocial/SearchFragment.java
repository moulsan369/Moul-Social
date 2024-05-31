package com.ntu.moulsocial;

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

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText editTextSearch;
    private RecyclerView recyclerViewSearchResults;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<Post> filteredPostList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults);

        postList = new ArrayList<>();
        filteredPostList = new ArrayList<>();

        // Add sample data
        postList.add(new Post("John Doe's post content", null));
        postList.add(new Post("Jane Smith's post content", null));

        postAdapter = new PostAdapter(filteredPostList, null);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearchResults.setAdapter(postAdapter);

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
