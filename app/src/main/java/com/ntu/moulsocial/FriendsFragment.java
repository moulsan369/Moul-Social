package com.ntu.moulsocial;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerViewFriends;
    private FriendAdapter friendAdapter;
    private List<Friend> friendList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerViewFriends = view.findViewById(R.id.recyclerViewFriends);

        friendList = new ArrayList<>();
        // Add sample data
        friendList.add(new Friend("John Doe", null));
        friendList.add(new Friend("Jane Smith", null));

        friendAdapter = new FriendAdapter(friendList);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFriends.setAdapter(friendAdapter);

        return view;
    }
}
