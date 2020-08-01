package com.himanshu.mynotes.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.mynotes.EditProfileActivity;
import com.himanshu.mynotes.R;
import com.himanshu.mynotes.adapter.ProfileItemAdapter;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private TextView ProfileName, ProfileEmail;
    private ImageView ProfileImage;
    private Button EditProfileBtn;
    private RecyclerView recyclerView;
    private CardView ProfileCardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ProfileName = view.findViewById(R.id.profile_name);
        ProfileEmail = view.findViewById(R.id.profile_email);
        ProfileImage = view.findViewById(R.id.profile_image);
        ProfileCardView = view.findViewById(R.id.profile_image_card_view);

        String[] title = {"Pin", "Archive", "Bin", "About", "Log Out"};
        int[] image = {
                R.drawable.ic_pushpin,
                R.drawable.archive_icon,
                R.drawable.ic_delete,
                R.drawable.ic_about,
                R.drawable.logout};
        recyclerView = view.findViewById(R.id.profile_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ProfileItemAdapter(getContext(), title, image));

        EditProfileBtn = view.findViewById(R.id.profile_edit);
        EditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(ProfileCardView, "card_transition");
                pairs[1] = new Pair<View, String>(ProfileName, "name_transition");
                pairs[2] = new Pair<View, String>(EditProfileBtn, "title_transition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                startActivity(i, options.toBundle());
            }
        });
        retrieveUserInfo();
        return view;
    }


    private void retrieveUserInfo() {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("userDetails")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ProfileName.setText(snapshot.child("name").getValue().toString());
                    ProfileEmail.setText(snapshot.child("emailId").getValue().toString());
                    if (!snapshot.child("photoUrl").getValue().toString().equals("")) {
                        Picasso.with(ProfileImage.getContext()).load(snapshot.child("photoUrl").getValue().toString())
                                .placeholder(R.drawable.profilemale).into(ProfileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}