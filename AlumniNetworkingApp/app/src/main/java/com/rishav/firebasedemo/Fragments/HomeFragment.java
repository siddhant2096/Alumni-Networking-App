package com.rishav.firebasedemo.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rishav.firebasedemo.Adapter.PostAdapter;
import com.rishav.firebasedemo.ChatMain;
import com.rishav.firebasedemo.EventActivity;
import com.rishav.firebasedemo.JobActivity;
import com.rishav.firebasedemo.LoginActivity;
import com.rishav.firebasedemo.Model.Post;
import com.rishav.firebasedemo.Model.User;
import com.rishav.firebasedemo.R;
import com.rishav.firebasedemo.webviewactivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
private ImageView logo;
private ImageView pic;
    private List<String> followingList;
    private FirebaseUser fUser;
    private ImageView image_icon;
    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");

        if (data.equals("none")) {
            profileId = fUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }
logo=view.findViewById(R.id.logo);
image_icon=view.findViewById(R.id.image_icon);
        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("== My activity ===","OnClick is called");
                Intent intent = new Intent( getActivity(), webviewactivity.class);
                startActivity(intent);
            }
        });
        followingList = new ArrayList<>();
        ImageButton chat_button=(ImageButton)view.findViewById(R.id.chat_button);
        chat_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), ChatMain.class);
                startActivity(intent);
            }


        });
        Button job_button=(Button)view.findViewById(R.id.job_button);
       job_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), JobActivity.class);
                startActivity(intent);
            }


        });
        Button event_button=(Button)view.findViewById(R.id.event_button);
       event_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), EventActivity.class);
                startActivity(intent);
            }


        });

        userInfo();
        checkFollowingUsers();
        return view;
    }

    private void checkFollowingUsers() {

        FirebaseDatabase.getInstance("https://alumni-netwroking-app-default-rtdb.firebaseio.com/").getReference().child("Follow").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void userInfo() {

        FirebaseDatabase.getInstance("https://alumni-netwroking-app-default-rtdb.firebaseio.com/").getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getImageurl().equals("default"))
                {image_icon.setImageResource(R.drawable.admin_item);}
                else
                {Picasso.get().load(user.getImageurl()).into(image_icon);}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readPosts() {

        FirebaseDatabase.getInstance("https://alumni-netwroking-app-default-rtdb.firebaseio.com/").getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)){

                            postList.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
